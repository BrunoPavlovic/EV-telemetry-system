package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Odgovor;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Vozilo;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.VoznjeDAO;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/simulacije")
public class SimulacijeResurs extends SviResursi {
  private VoznjeDAO voznjeDAO = null;
  private String adresaVozila;
  private int mreznaVrataVozila;

  @PostConstruct
  private void pripremiVoznjeDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      preuzmiPostavke("NWTiS_REST_S.txt");
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.voznjeDAO = new VoznjeDAO(vezaBP);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * Dohvaća voznje u intervalu
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param od od vremena
   * @param do do vremena
   * @return lista voznji
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVoznje(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    return Response.status(Response.Status.OK)
        .entity(voznjeDAO.dohvatiVoznje(odVremena, doVremena).toArray()).build();
  }

  /**
   * Dohvaća voznje za definirano vozilo
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @param odVremena
   * @param doVremena
   * @return lista voznji
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVoznjeVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK).entity(voznjeDAO.dohvatiVoznjeVozila(id).toArray())
          .build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(voznjeDAO.dohvatiVoznjeVozila(id, odVremena, doVremena).toArray()).build();
    }
  }

  /**
   * Dodaje novu voznju.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaKazna podaci nove voznje
   * @return OK ako je voznja uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajVoznju(@HeaderParam("Accept") String tipOdgovora,
      Vozilo novaVoznja) {

    var komanda = new StringBuilder();
    var razmak = " ";

    komanda.append("VOZILO").append(razmak).append(novaVoznja.getId()).append(razmak)
        .append(novaVoznja.getBroj()).append(razmak).append(novaVoznja.getVrijeme()).append(razmak)
        .append(novaVoznja.getBrzina()).append(razmak).append(novaVoznja.getSnaga()).append(razmak)
        .append(novaVoznja.getStruja()).append(razmak).append(novaVoznja.getVisina()).append(razmak)
        .append(novaVoznja.getGpsBrzina()).append(razmak).append(novaVoznja.getTempVozila())
        .append(razmak).append(novaVoznja.getPostotakBaterija()).append(razmak)
        .append(novaVoznja.getNaponBaterija()).append(razmak)
        .append(novaVoznja.getKapacitetBaterija()).append(razmak)
        .append(novaVoznja.getTempBaterija()).append(razmak).append(novaVoznja.getPreostaloKm())
        .append(razmak).append(novaVoznja.getUkupnoKm()).append(razmak)
        .append(novaVoznja.getGpsSirina()).append(razmak).append(novaVoznja.getGpsDuzina())
        .append("\n");

    var saljiNaRadnika = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaVozila,
        this.mreznaVrataVozila, komanda.toString());

    if (saljiNaRadnika == null) {
      var odgovor = voznjeDAO.dodajVoznju(novaVoznja);
      if (odgovor) {
        return Response.status(Response.Status.OK)
            .entity(new Odgovor("OK", "Uspješan upis nove vožnje.")).build();
      } else {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new Odgovor("ERROR", "Neuspješan upis nove vožnje u bazu.")).build();
      }
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(new Odgovor("ERROR", "Greška kod radnika za vozila")).build();
    }
  }

  /**
   * Preuzmi postavke.
   *
   * @param nazivDatoteke naziv datoteke
   * @throws Exception iznimka
   */
  private void preuzmiPostavke(String nazivDatoteke) throws Exception {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

    this.adresaVozila = konfig.dajPostavku("adresaVozila");
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
  }

}
