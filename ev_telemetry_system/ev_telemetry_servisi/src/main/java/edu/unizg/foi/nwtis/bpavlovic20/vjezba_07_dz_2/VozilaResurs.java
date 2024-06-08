package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2;

import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Odgovor;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PraceneVoznjeDAO;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Vozilo;
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

@Path("nwtis/v1/api/vozila")
public class VozilaResurs extends SviResursi {
  private PraceneVoznjeDAO praceneVoznjeDAO = null;
  private String adresaVozila;
  private int mreznaVrataVozila;

  @PostConstruct
  private void pripremiPraceneVoznjeDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      preuzmiPostavke("NWTiS_REST_V.txt");
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.praceneVoznjeDAO = new PraceneVoznjeDAO(vezaBP);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }


  /**
   * Dohvaća pracene voznje u intervalu
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param od od vremena
   * @param do do vremena
   * @return lista pracenih voznji
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonPraceneVoznje(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    return Response.status(Response.Status.OK)
        .entity(praceneVoznjeDAO.dohvatiPracenjeVoznje(odVremena, doVremena).toArray()).build();
  }

  /**
   * Dohvaća pracene voznje za definirano vozilo
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @param odVremena
   * @param doVremena
   * @return lista pracenih voznji
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonPraceneVoznjeVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    if (odVremena <= 0 || doVremena <= 0) {
      return Response.status(Response.Status.OK)
          .entity(praceneVoznjeDAO.dohvatiPraceneVoznjeVozila(id).toArray()).build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(praceneVoznjeDAO.dohvatiPraceneVoznjeVozila(id, odVremena, doVremena).toArray())
          .build();
    }
  }

  /**
   * Pokreće praćenje vožnje za e-vozilo
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @return OK ili ERROR
   */
  @Path("/vozilo/{id}/start")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getStartPraceneVoznje(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaVozila, this.mreznaVrataVozila,
        "VOZILO START " + id + "\n");

    return Response.status(Response.Status.OK)
        .entity(new Odgovor("OK", "Uspješan pokretanje praćene vožnje.")).build();
  }

  /**
   * Zaustavlja praćenje vožnje za e-vozilo
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param id vozila
   * @return OK ili ERROR
   */
  @Path("/vozilo/{id}/stop")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getStopPraceneVoznje(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaVozila, this.mreznaVrataVozila,
        "VOZILO STOP " + id + "\n");

    return Response.status(Response.Status.OK)
        .entity(new Odgovor("OK", "Uspješan zaustavljanje praćene vožnje.")).build();
  }

  /**
   * Dodaje novu pracenu voznju.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novaKazna podaci nove pracene voznje
   * @return OK ako je pracena voznja uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajPracenuVoznju(@HeaderParam("Accept") String tipOdgovora,
      Vozilo novaPracenaVoznja) {

    var odgovor = praceneVoznjeDAO.dodajPracenuVoznji(novaPracenaVoznja);
    if (odgovor) {
      return Response.status(Response.Status.OK)
          .entity(new Odgovor("OK", "Uspješan upis praćene vožnje.")).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(new Odgovor("ERROR", "Neuspješan upis praćene vožnje u bazu.")).build();
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
