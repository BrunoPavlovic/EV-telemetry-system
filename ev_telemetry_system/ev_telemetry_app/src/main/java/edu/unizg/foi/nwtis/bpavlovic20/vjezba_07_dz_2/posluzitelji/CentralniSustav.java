package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa CentralniSustav.
 */
public class CentralniSustav {

  /** Mrezna vrata radara. */
  public int mreznaVrataRadara;

  /** Mrezna vrata vozila. */
  public int mreznaVrataVozila;

  /** Mrezna vrata nadzora. */
  public int mreznaVrataNadzora;

  /** Maks vozila. */
  public int maksVozila;

  /** Tvornica dretvi. */
  public ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /** Kolekcija svi radari. */
  public ConcurrentHashMap<Integer, PodaciRadara> sviRadari =
      new ConcurrentHashMap<Integer, PodaciRadara>();

  /** Kolekcija sva vozila. */
  ConcurrentHashMap<Integer, RedPodaciVozila> svaVozila =
      new ConcurrentHashMap<Integer, RedPodaciVozila>();

  /** Kolekcija id-jeva rest vozila. */
  public List<Integer> restVozila = new ArrayList<Integer>();


  /**
   * Main metoda.
   *
   * @param args - argumenti dani prilikom pokretanja klase
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    CentralniSustav centralniSustav = new CentralniSustav();
    try {
      centralniSustav.preuzmiPostavke(args);

      centralniSustav.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokreni posluzitelja.
   * 
   * pokreće dvije virtualne dretve : PosluziteljZaRegistracijuRadara i PosluziteljZaVozila
   * 
   */
  public void pokreniPosluzitelja() {
    PosluziteljZaRegistracijuRadara posluziteljZaRegistracijuRadara =
        new PosluziteljZaRegistracijuRadara(this.mreznaVrataRadara, this);
    PosluziteljZaVozila posluziteljZaVozila = new PosluziteljZaVozila(this.mreznaVrataVozila, this);

    Thread virtualnaDretvaRegistracijaRadara =
        tvornicaDretvi.newThread(posluziteljZaRegistracijuRadara);
    var virtualnaDretvaVozila = tvornicaDretvi.newThread(posluziteljZaVozila);

    virtualnaDretvaRegistracijaRadara.start();
    virtualnaDretvaVozila.start();

    try {
      virtualnaDretvaRegistracijaRadara.join();
      virtualnaDretvaVozila.join();
    } catch (InterruptedException e) {
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

    this.mreznaVrataRadara = Integer.valueOf(konfig.dajPostavku("mreznaVrataRadara"));
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.mreznaVrataNadzora = Integer.valueOf(konfig.dajPostavku("mreznaVrataNadzora"));
    this.maksVozila = Integer.valueOf(konfig.dajPostavku("maksVozila"));
  }
}
