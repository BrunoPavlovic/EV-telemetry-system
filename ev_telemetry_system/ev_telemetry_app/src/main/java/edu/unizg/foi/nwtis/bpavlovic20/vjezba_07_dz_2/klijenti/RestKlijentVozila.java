package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.klijenti;

import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Vozilo;
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
