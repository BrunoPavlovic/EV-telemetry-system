package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.klijenti;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa Klijent.
 */
public class Klijent {

  /** Adresa kazne. */
  private String adresaKazne;

  /** Mrezna vrata kazne. */
  private Integer mreznaVrataKazne;

  /**
   * Main metoda.
   *
   * @param args - argumenti dani prilikom pokretanja klase
   */
  public static void main(String[] args) {
    if (args.length != 3 && args.length != 4) {
      System.out.println("Broj argumenata treba biti 3 ili 4.");
      return;
    }

    Klijent klijent = new Klijent();
    try {
      klijent.preuzmiPostavke(args);
      klijent.posaljiKomandu(args);
    } catch (NumberFormatException | UnknownHostException | NeispravnaKonfiguracija e) {
      e.printStackTrace();
    }

  }

  /**
   * Posalji komandu.
   *
   * @param args - argumenti dani prilikom pokretanja klase
   */
  private void posaljiKomandu(String[] args) {
    String komanda = null;
    if (args.length == 3) {
      komanda = "STATISTIKA " + args[1] + " " + args[2] + "\n";

    } else if (args.length == 4) {
      komanda = "VOZILO " + args[1] + " " + args[2] + " " + args[3] + "\n";
    }

    MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaKazne, this.mreznaVrataKazne, komanda);

  }

  /**
   * Preuzmi postavke.
   *
   * Preuzima postavke iz konfiguracijske datoteke.
   *
   * @param args argumenti dani prilikom pokretanja klase
   * @throws NeispravnaKonfiguracija
   * @throws NumberFormatException
   * @throws UnknownHostException
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.adresaKazne = konfig.dajPostavku("adresaKazne");
    this.mreznaVrataKazne = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
  }

}
