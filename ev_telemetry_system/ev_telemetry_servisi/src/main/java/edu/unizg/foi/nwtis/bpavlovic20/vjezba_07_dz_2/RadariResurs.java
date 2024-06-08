package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Odgovor;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Radar;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/radari")
public class RadariResurs extends SviResursi {
  private String adresaRegistracije;
  private int mreznaVrataRegistracije;

  @PostConstruct
  public void pripremiResurs() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      preuzmiPostavke("NWTiS_REST_R.txt");
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonSviRadari(@HeaderParam("Accept") String tipOdgovora) {
    var odgovor = posaljiZahtjev("RADAR SVI\n");
    List<Radar> radari = pretvoriUPodatkeRadara(odgovor);
    return Response.status(Response.Status.OK).entity(radari).build();
  }

  @Path("{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonRadar(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
    var odgovor = posaljiZahtjev("RADAR SVI\n");
    List<Radar> radari = pretvoriUPodatkeRadara(odgovor);
    Radar podaciRadara =
        radari.stream().filter(radar -> radar.getId() == id).findFirst().orElse(null);

    if (podaciRadara != null) {
      return Response.status(Response.Status.OK).entity(podaciRadara).build();
    } else {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(new Odgovor("ERROR", "Radar s id:" + id + "nije pronađen")).build();
    }
  }

  @Path("/reset")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonResetRadari(@HeaderParam("Accept") String tipOdgovora) {
    var o = posaljiZahtjev("RADAR RESET\n");
    Odgovor odgovor = jsonOdgovor(o);
    return Response.status(Response.Status.OK).entity(odgovor).build();
  }

  @Path("{id}/provjeri")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonProvjeriRadar(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append(id).append("\n");

    var o = posaljiZahtjev(komanda.toString());
    if (o.contains("OK")) {
      return Response.status(Response.Status.OK).entity(new Odgovor(o, "Aktivan radar " + id))
          .build();
    }

    var odgovor = jsonOdgovor(o);
    return Response.status(Response.Status.NOT_FOUND).entity(odgovor).build();
  }

  @DELETE
  @Produces({MediaType.APPLICATION_JSON})
  public Response deleteJsonSviRadari(@HeaderParam("Accept") String tipOdgovora) {
    var o = posaljiZahtjev("RADAR OBRIŠI SVE\n");
    var odgovor = new Odgovor(o, "Radari su uspješno izbrisani");
    return Response.status(Response.Status.OK).entity(odgovor).build();
  }

  @Path("{id}")
  @DELETE
  @Produces({MediaType.APPLICATION_JSON})
  public Response deleteJsonRadar(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var komanda = new StringBuilder();
    komanda.append("RADAR OBRIŠI ").append(id).append("\n");

    var o = posaljiZahtjev(komanda.toString());
    if (o.contains("OK")) {
      var odgovor = new Odgovor(o, "Uspješno obrisan radar" + id);
      return Response.status(Response.Status.OK).entity(odgovor).build();
    }

    var odgovor = jsonOdgovor(o);
    return Response.status(Response.Status.NOT_FOUND).entity(odgovor).build();
  }

  public String posaljiZahtjev(String komanda) {
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRegistracije,
        this.mreznaVrataRegistracije, komanda);
    return odgovor;
  }

  private Odgovor jsonOdgovor(String o) {
    var dio = o.split(" ", 2);
    return new Odgovor(dio[0], dio[1]);
  }

  public List<Radar> pretvoriUPodatkeRadara(String odgovor) {
    List<Radar> radari = new ArrayList<>();

    var dio = odgovor.split(" ", 2);
    String status = dio[0];
    String sadrzaj = dio[1];

    sadrzaj = sadrzaj.substring(1, sadrzaj.length() - 1);
    System.out.println("Sadrzaj: " + sadrzaj);
    String[] zapisiRadara = sadrzaj.split("],\\s*\\[");

    for (String zapis : zapisiRadara) {
      zapis = zapis.replace("[", "").replace("]", "");
      String[] podaci = zapis.trim().split("\\s+");

      Radar radar = new Radar(Integer.parseInt(podaci[0]), podaci[1], Integer.parseInt(podaci[2]),
          -1, -1, Integer.parseInt(podaci[5]), null, -1, null, -1, null,
          Double.parseDouble(podaci[3]), Double.parseDouble(podaci[4]));

      radari.add(radar);
    }

    return radari;
  }

  /**
   * Preuzmi postavke.
   *
   * @param nazivDatoteke naziv datoteke
   * @throws Exception iznimka
   */
  private void preuzmiPostavke(String nazivDatoteke) throws Exception {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

    this.adresaRegistracije = konfig.dajPostavku("adresaRegistracije");
    this.mreznaVrataRegistracije = Integer.valueOf(konfig.dajPostavku("mreznaVrataRegistracije"));
  }

}
