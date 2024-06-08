package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaRadare;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa PosluziteljRadara.
 */
public class PosluziteljRadara {

  /** Tvornica dretvi. */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /** Podaci radara. */
  public PodaciRadara podaciRadara;

  /** Kolekcija sva brza vozila. */
  ConcurrentHashMap<Integer, BrzoVozilo> svaBrzaVozila =
      new ConcurrentHashMap<Integer, BrzoVozilo>();

  /**
   * Main metoda.
   * 
   * ovisno o broju i tipu argumenata poziva funkcije
   *
   * @param args - argumenti dani prilikom pokretanja klase
   */
  public static void main(String[] args) {
    if (args.length != 1 && args.length != 3) {
      System.out.println("Broj argumenata treba biti 1 ili 3.");
      return;
    }

    PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
    try {

      posluziteljRadara.preuzmiPostavke(args);
      if (args.length == 1) {
        posluziteljRadara.registrirajPosluzitelja();
        posluziteljRadara.pokreniPosluzitelja();
      } else if (args.length == 3 && posluziteljRadara.jeLiCijeliBroj(args[2])) {
        posluziteljRadara.obrisiRadar();
      } else {
        posluziteljRadara.obrisiSveRadare();
      }

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println("ERROR 39 " + e.getMessage());
      return;
    }
  }

  /**
   * Je li cijeli broj.
   * 
   * provjerava je li argument cijeli broj-
   *
   * @param argument tipa String
   * @return true ako je cijeli broj inače false
   */
  public boolean jeLiCijeliBroj(String argument) {
    try {
      Integer.parseInt(argument);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Obrisi sve radare.
   * 
   * Briše sve registrirane radare.
   *
   * @return true ako je uspješno
   */
  public boolean obrisiSveRadare() {
    String komanda = "RADAR OBRIŠI SVE\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda);

    if (odgovor != null) {
      return true;
    }
    return false;

  }

  /**
   * Obrisi radar.
   * 
   * Briše radar prema id-u.
   *
   * @return true ako je uspješno
   */
  private boolean obrisiRadar() {
    String komanda = "RADAR OBRIŠI" + " " + this.podaciRadara.id() + "\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda);

    if (odgovor != null) {
      return true;
    }
    return false;

  }

  /**
   * Registriraj posluzitelja.
   * 
   * Šalje zahtjev za registriraciju radara
   *
   * @return true ako je uspješno
   */
  public boolean registrirajPosluzitelja() {
    String komanda = "RADAR" + " " + this.podaciRadara.id() + " " + this.podaciRadara.adresaRadara()
        + " " + this.podaciRadara.mreznaVrataRadara() + " " + this.podaciRadara.gpsSirina() + " "
        + this.podaciRadara.gpsDuzina() + " " + this.podaciRadara.maksUdaljenost() + "\n";

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda);

    if (odgovor != null) {
      return true;
    }
    return false;
  }

  /**
   * Pokreni posluzitelja.
   * 
   * Otvara mrežnu utičnicu te kreira novu virtualnu dretvu koja izvršava objekt klase
   * RadnikZaRadare za svakog klijenta.
   * 
   */
  public void pokreniPosluzitelja() {
    boolean kraj = false;
    try (ServerSocket mreznaUticnicaPosluzitelja =
        new ServerSocket(this.podaciRadara.mreznaVrataRadara())) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        var radnik =
            new RadnikZaRadare(mreznaUticnica, this.podaciRadara, this.svaBrzaVozila, this);
        var dretva = tvornicaDretvi.newThread(radnik);

        dretva.start();
        try {
          dretva.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Preuzmi postavke.
   * 
   * Preuzima postavke iz konfiguracijske datoteke.
   *
   * @param args - argumenti dani prilikom pokretanja klase
   * @throws NeispravnaKonfiguracija
   * @throws NumberFormatException
   * @throws UnknownHostException
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.podaciRadara = new PodaciRadara(Integer.parseInt(konfig.dajPostavku("id")),
        InetAddress.getLocalHost().getHostName(),
        Integer.parseInt(konfig.dajPostavku("mreznaVrataRadara")),
        Integer.parseInt(konfig.dajPostavku("maksBrzina")),
        Integer.parseInt(konfig.dajPostavku("maksTrajanje")),
        Integer.parseInt(konfig.dajPostavku("maksUdaljenost")),
        konfig.dajPostavku("adresaRegistracije"),
        Integer.parseInt(konfig.dajPostavku("mreznaVrataRegistracije")),
        konfig.dajPostavku("adresaKazne"), Integer.parseInt(konfig.dajPostavku("mreznaVrataKazne")),
        konfig.dajPostavku("postanskaAdresaRadara"),
        Double.parseDouble(konfig.dajPostavku("gpsSirina")),
        Double.parseDouble(konfig.dajPostavku("gpsDuzina")));
  }
}

