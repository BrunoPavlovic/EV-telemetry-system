package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Odgovor;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Radar;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentRadari {

  public RestKlijentRadari() {}

  public List<Radar> getRadariJSON() {
    RestRadari rr = new RestRadari();
    List<Radar> radari = rr.getJSON_sviRadari();
    return radari;
  }

  public Radar getRadarJSON(String id) {
    RestRadari rr = new RestRadari();
    Radar radar = rr.getJSON_radar(id);
    return radar;
  }

  public Odgovor getProvjeriRadarJSON(String id) {
    RestRadari rr = new RestRadari();
    Odgovor odgovor = rr.getJSON_provjeriRadar(id);
    return odgovor;
  }

  public Odgovor getResetRadarJSON() {
    RestRadari rr = new RestRadari();
    Odgovor odgovor = rr.getJSON_reset();
    return odgovor;
  }

  public Odgovor obrisiSveRadare() {
    RestRadari rr = new RestRadari();
    Odgovor odgovor = rr.getJSON_obrisiSve();
    return odgovor;
  }

  public Odgovor obrisiRadar(String id) {
    RestRadari rr = new RestRadari();
    Odgovor odgovor = rr.getJSON_obrisiRadar(id);
    return odgovor;
  }

  static class RestRadari {
    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** knstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestRadari() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/radari");
    }

    /**
     * Vraća sve radare.
     *
     * @return radari
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Radar> getJSON_sviRadari() throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Radar> radari = new ArrayList<Radar>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pradari = jb.fromJson(odgovor, Radar[].class);
        radari.addAll(Arrays.asList(pradari));
      }

      return radari;
    }

    /**
     * Vraća radar s id.
     * 
     * @param id id radara
     * @return radari
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Radar getJSON_radar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var radar = jb.fromJson(odgovor, Radar.class);
        return radar;
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
     * Pokreće postupak resetiranja svih radara.
     * 
     * @return Odgovor odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Odgovor getJSON_reset() throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path("reset");
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var odgovorReset = jb.fromJson(odgovor, Odgovor.class);
        return odgovorReset;
      }

      return null;
    }

    /**
     * Pokreće postupak brisanja svih radara.
     * 
     * @return Odgovor odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Odgovor getJSON_obrisiSve() throws ClientErrorException {
      WebTarget resource = webTarget;
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().delete();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var odgovorObrisiSve = jb.fromJson(odgovor, Odgovor.class);
        return odgovorObrisiSve;
      }

      return null;
    }

    /**
     * Pokreće brisanje radara.
     * 
     * @param id id radara
     * @return Odgovor odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Odgovor getJSON_obrisiRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().delete();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var obrisiRadarOdgovor = jb.fromJson(odgovor, Odgovor.class);
        return obrisiRadarOdgovor;
      }

      return null;
    }
  }

}
