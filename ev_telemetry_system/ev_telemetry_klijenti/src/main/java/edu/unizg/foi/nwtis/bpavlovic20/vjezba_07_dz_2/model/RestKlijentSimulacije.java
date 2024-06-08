package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

public class RestKlijentSimulacije {

  /**
   * Vraća vožnje u intervalu od do.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return vožnje
   */
  public List<Vozilo> getSimulacijeJSON_od_do(long odVremena, long doVremena) {
    RestSimulacije rs = new RestSimulacije();
    List<Vozilo> voznje = rs.getJSON_od_do(odVremena, doVremena);

    return voznje;
  }

  /**
   * Vraća vožnje za vozilo.
   *
   * @param id id vozila
   * @return vožnje
   */
  public List<Vozilo> getSimulacijeJSON_vozilo(String id) {
    RestSimulacije rs = new RestSimulacije();
    List<Vozilo> voznje = rs.getJSON_vozilo(id);

    return voznje;
  }

  /**
   * Vraća vožnje za vozilo u intervalu od do..
   *
   * @param id id vozila
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return vožnje
   */
  public List<Vozilo> getSimulacijeJSON_vozilo_od_do(String id, long odVremena, long doVremena) {
    RestSimulacije rs = new RestSimulacije();
    List<Vozilo> voznje = rs.getJSON_vozilo_od_do(id, odVremena, doVremena);

    return voznje;
  }

  /**
   * Dodaje vožnju.
   *
   * @param Vozilo vožnja
   * @return true, ako je uspješno
   */
  public boolean postSimulacijeJSON(Vozilo voznja) {
    RestSimulacije rs = new RestSimulacije();
    var odgovor = rs.postJSON(voznja);
    return odgovor;
  }

  static class RestSimulacije {
    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** knstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestSimulacije() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/simulacije");
    }

    /**
     * Vraća vožnje u intervalu od do.
     *
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return vožnje
     * @throws ClientErrorException iznimka kod poziva klijentan
     */
    public List<Vozilo> getJSON_od_do(long odVremena, long doVremena) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> voznje = new ArrayList<Vozilo>();

      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvozila = jb.fromJson(odgovor, Vozilo[].class);
        voznje.addAll(Arrays.asList(pvozila));
      }

      return voznje;
    }


    /**
     * Vraća vožnje za vozilo.
     *
     * @param id id vozila
     * @return vožnje
     * @throws ClientErrorException iznimka kod poziva klijentaon
     */
    public List<Vozilo> getJSON_vozilo(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> voznje = new ArrayList<Vozilo>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvozila = jb.fromJson(odgovor, Vozilo[].class);
        voznje.addAll(Arrays.asList(pvozila));
      }

      return voznje;
    }

    /**
     * Vraća vožnje za vozilo u intervalu od do..
     *
     * @param id id vozila
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Vozilo> getJSON_vozilo_od_do(String id, long odVremena, long doVremena)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> voznje = new ArrayList<Vozilo>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("od", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvozila = jb.fromJson(odgovor, Vozilo[].class);
        voznje.addAll(Arrays.asList(pvozila));
      }

      return voznje;
    }

    /**
     * Dodaje vožnju.
     *
     * @param Vozilo vožnja
     * @return true, ako je uspješno
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public boolean postJSON(Vozilo voznja) throws ClientErrorException {
      WebTarget resource = webTarget;
      if (voznja == null) {
        return false;
      }
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      var odgovor = request.post(Entity.entity(voznja, MediaType.APPLICATION_JSON));
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
