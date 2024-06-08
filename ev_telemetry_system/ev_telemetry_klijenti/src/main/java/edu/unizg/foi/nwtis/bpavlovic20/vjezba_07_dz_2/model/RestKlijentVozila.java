package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Odgovor;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Vozilo;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentVozila {

  public RestKlijentVozila() {}

  /**
   * Vraća praćene vožnje u intervalu od do.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return praćene vožnje
   */
  public List<Vozilo> getPracenaVoznjaJSON_od_do(long odVremena, long doVremena) {
    RestVozila rv = new RestVozila();
    List<Vozilo> praceneVoznje = rv.getJSON_od_do(odVremena, doVremena);

    return praceneVoznje;
  }

  /**
   * Vraća praćene vožnje za vozilo.
   *
   * @param id id vozila
   * @return praćene vožnje
   */
  public List<Vozilo> getPracenaVoznjaJSON_vozilo(String id) {
    RestVozila rv = new RestVozila();
    List<Vozilo> praceneVoznje = rv.getJSON_vozilo(id);

    return praceneVoznje;
  }

  /**
   * Vraća praćene vožnje za vozilo u intervalu od do..
   *
   * @param id id vozila
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return praćene vožnje
   */
  public List<Vozilo> getPracenaVoznjaJSON_vozilo_od_do(String id, long odVremena, long doVremena) {
    RestVozila rv = new RestVozila();
    List<Vozilo> praceneVoznje = rv.getJSON_vozilo_od_do(id, odVremena, doVremena);

    return praceneVoznje;
  }

  /**
   * Pokreće praćenje vožnje za e-vozilo.
   * 
   * @param id id vozila
   * @return Odgovor odgovor
   * @throws ClientErrorException iznimka kod poziva klijenta
   */
  public Odgovor getPracenaVoznjaJSON_start(String id) {
    RestVozila rv = new RestVozila();
    Odgovor odgovor = rv.getJSON_start(id);
    return odgovor;
  }

  /**
   * Zaustavlja praćenje vožnje za e-vozilo.
   * 
   * @param id id vozila
   * @return Odgovor odgovor
   * @throws ClientErrorException iznimka kod poziva klijenta
   */
  public Odgovor getPracenaVoznjaJSON_stop(String id) {
    RestVozila rv = new RestVozila();
    Odgovor odgovor = rv.getJSON_stop(id);
    return odgovor;
  }


  /**
   * Dodaje praćenu vožnju.
   *
   * @param Vozilo praćena vožnja
   * @return true, ako je uspješno
   */
  public boolean postPracenaVoznjaJSON(Vozilo pracenaVoznja) {
    RestVozila rv = new RestVozila();
    var odgovor = rv.postJSON(pracenaVoznja);
    return odgovor;
  }

  static class RestVozila {
    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** knstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestVozila() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/vozila");
    }

    /**
     * Vraća praćene vožnje u intervalu od do.
     *
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return praćene vožnje
     * @throws ClientErrorException iznimka kod poziva klijentan
     */
    public List<Vozilo> getJSON_od_do(long odVremena, long doVremena) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> praceneVoznje = new ArrayList<Vozilo>();

      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvozila = jb.fromJson(odgovor, Vozilo[].class);
        praceneVoznje.addAll(Arrays.asList(pvozila));
      }

      return praceneVoznje;
    }


    /**
     * Vraća praćene vožnje za vozilo.
     *
     * @param id id vozila
     * @return praćene vožnje
     * @throws ClientErrorException iznimka kod poziva klijentaon
     */
    public List<Vozilo> getJSON_vozilo(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> praceneVoznje = new ArrayList<Vozilo>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvozila = jb.fromJson(odgovor, Vozilo[].class);
        praceneVoznje.addAll(Arrays.asList(pvozila));
      }

      return praceneVoznje;
    }

    /**
     * Vraća praćene vožnje za vozilo u intervalu od do..
     *
     * @param id id vozila
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return praćene vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Vozilo> getJSON_vozilo_od_do(String id, long odVremena, long doVremena)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> praceneVoznje = new ArrayList<Vozilo>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("od", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvozila = jb.fromJson(odgovor, Vozilo[].class);
        praceneVoznje.addAll(Arrays.asList(pvozila));
      }

      return praceneVoznje;
    }

    /**
     * Pokreće praćene vožnje za e-vozilo.
     * 
     * @param id id vozila
     * @return Odgovor odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Odgovor getJSON_start(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("vozilo/{0}/start", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var startOdgovor = jb.fromJson(odgovor, Odgovor.class);
        return startOdgovor;
      }

      return null;
    }

    /**
     * Zaustavlja praćene vožnje za e-vozilo.
     * 
     * @param id id vozila
     * @return Odgovor odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Odgovor getJSON_stop(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("vozilo/{0}/stop", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var stopOdgovor = jb.fromJson(odgovor, Odgovor.class);
        return stopOdgovor;
      }

      return null;
    }

    /**
     * Pokreće provjeru radara.
     * 
     * @param id id radara
     * @return Odgovor odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Odgovor getJSON_provjeriRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/provjeri", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var provjeriRadarOdgovor = jb.fromJson(odgovor, Odgovor.class);
        return provjeriRadarOdgovor;
      }

      return null;
    }

    /**
     * Dodaje praćenu vožnju.
     *
     * @param Vozilo praćena vožnja
     * @return true, ako je uspješno
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean postJSON(Vozilo pracenaVoznja) throws ClientErrorException {
      WebTarget resource = webTarget;
      if (pracenaVoznja == null) {
        return false;
      }
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      var odgovor = request.post(Entity.entity(pracenaVoznja, MediaType.APPLICATION_JSON));
      if (odgovor.getStatus() == Response.Status.OK.getStatusCode()) {
        return true;
      }

      return false;
    }

    /**
     * Close.
     */
    public void close() {
      client.close();
    }
  }

}
