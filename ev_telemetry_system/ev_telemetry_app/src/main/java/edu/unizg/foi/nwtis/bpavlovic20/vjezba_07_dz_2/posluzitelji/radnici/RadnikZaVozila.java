package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.radnici;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.klijenti.RestKlijentVozila;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Vozilo;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.CentralniSustav;

/**
 * The Class RadnikZaVozila.
 */
public class RadnikZaVozila implements Runnable {

  /** Klijent. */
  private AsynchronousSocketChannel klijent;
  private CentralniSustav centralniSustav;

  /** Kolekcija svi radari. */
  private ConcurrentHashMap<Integer, PodaciRadara> sviRadari;

  /** Predlozak posluzitelj za vozila. */
  Pattern predlozakPosluziteljZaVozila = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<snaga>-?\\d+([.]\\d+)?) (?<struja>-?\\d+([.]\\d+)?) (?<visina>\\d+([.]\\d+)?) (?<gpsBrzina>-?\\d+([.]\\d+)?) (?<tempVozila>\\d+) (?<postotakBaterija>\\d+) (?<naponBaterija>\\d+([.]\\d+)?) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+) (?<preostaloKm>\\d+([.]\\d+)?) (?<ukupnoKm>\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

  Pattern predlozakStartVozilo = Pattern.compile("^VOZILO START (?<id>\\d+)$");
  Pattern predlozakStopVozilo = Pattern.compile("^VOZILO STOP (?<id>\\d+)$");

  /** Poklapanje posluzitelj za vozila. */
  Matcher poklapanjePosluziteljZaVozila;
  Matcher poklapanjeStartVozilo;
  Matcher poklapanjeStopVozilo;

  /** Podaci vozila. */
  private PodaciVozila podaciVozila;

  /**
   * Instancira novog radnik za vozila.
   *
   * @param klijent
   * @param sviRadari
   * @param centralniSustav2
   * @param restVozila2
   */
  public RadnikZaVozila(AsynchronousSocketChannel klijent,
      ConcurrentHashMap<Integer, PodaciRadara> sviRadari, CentralniSustav centralniSustav) {
    this.klijent = klijent;
    this.sviRadari = sviRadari;
    this.centralniSustav = centralniSustav;
  }

  /**
   * Run.
   * 
   * Čita dolazne podatke, obrađuje ih i nastavlja čekati nove podatke sve dok je klijentski kanal
   * otvoren.
   */
  @Override
  public void run() {
    try {
      while (true) {
        if ((klijent != null) && (klijent.isOpen())) {
          var buffer = ByteBuffer.allocate(2048);
          Future<Integer> citac = klijent.read(buffer);
          int procitaniBajtovi = citac.get();

          if (procitaniBajtovi == -1) {
            break;
          }

          String redak = new String(buffer.array()).trim();
          obradaZahtjeva(redak);

          buffer.clear();
          this.klijent.close();
        } else {
          break;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Obrada zahtjeva.
   *
   * @param zahtjev tipa String
   * @return odgovor ili ERROR.
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 20 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaVozila(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 20 Neispravna sintaksa komande.";
  }

  /**
   * Obrada zahtjeva vozila.
   * 
   * U slučaju poklapanja preuzimaju se podaci vozila i provjerava nalazi li se vozilo u dosegu
   * radara.
   *
   * @param zahtjev tipa String
   * @return OK ako je sve u redu, null ako nije pronađeno poklapanje te ERROR u ostalim
   *         slučajevima.
   */
  public String obradaZahtjevaVozila(String zahtjev) {
    try {
      this.poklapanjePosluziteljZaVozila = this.predlozakPosluziteljZaVozila.matcher(zahtjev);
      this.poklapanjeStartVozilo = this.predlozakStartVozilo.matcher(zahtjev);
      this.poklapanjeStopVozilo = this.predlozakStopVozilo.matcher(zahtjev);

      var status = this.poklapanjePosluziteljZaVozila.matches();
      if (status) {
        preuzmiPodatkeVozila();
        if (provjeriEVozilo(podaciVozila.id())) {
          var o = posaljiNaRest();
          if (!o) {
            return "ERROR 21 praćena vožnja nije uspješno poslana na RESTful web servis (POST metoda nije uspješno obavljena).";
          }
        }
        dosegRadara();

        return "OK";
      } else if (this.poklapanjeStartVozilo.matches()) {
        var id = Integer.valueOf(this.poklapanjeStartVozilo.group("id"));
        if (!provjeriEVozilo(id)) {
          this.centralniSustav.restVozila.add(id);
        }

        return "OK";
      } else if (this.poklapanjeStopVozilo.matches()) {
        var id = Integer.valueOf(this.poklapanjeStopVozilo.group("id"));
        if (provjeriEVozilo(id)) {
          this.centralniSustav.restVozila.remove(id);
        }

        return "OK";
      }
      return null;
    } catch (Exception e) {
      return "ERROR 29 " + e.getMessage();
    }
  }


  /**
   * Salji na rest.
   * 
   * Šalje podatke vozila na REST servis zadužen za rad s podacima praćenih vožnji.
   */
  private boolean posaljiNaRest() {
    RestKlijentVozila rkv = new RestKlijentVozila();
    var vozilo = new Vozilo(podaciVozila.id(), podaciVozila.broj(), podaciVozila.vrijeme(),
        podaciVozila.brzina(), podaciVozila.snaga(), podaciVozila.struja(), podaciVozila.visina(),
        podaciVozila.gpsBrzina(), podaciVozila.tempVozila(), podaciVozila.postotakBaterija(),
        podaciVozila.naponBaterija(), podaciVozila.kapacitetBaterija(), podaciVozila.tempBaterija(),
        podaciVozila.preostaloKm(), podaciVozila.ukupnoKm(), podaciVozila.gpsSirina(),
        podaciVozila.gpsDuzina());
    var o = rkv.postPracenaVoznjaJSON(vozilo);
    return o;
  }

  /**
   * Provjera e-vozila.
   * 
   * Provjera postojanja e-vozila u listi e-vozila čiji se podaci šalju na rest.
   */
  private boolean provjeriEVozilo(Integer id) {
    for (var vozilo : this.centralniSustav.restVozila) {
      if (vozilo == id) {
        return true;
      }
    }

    return false;
  }

  /**
   * Doseg radara.
   * 
   * Provjerava nalazili se vozilo u dosegu radara, ako se nalazi obavještava poslužitelj radara o
   * tome.
   */
  private void dosegRadara() {
    this.sviRadari.forEach((ključ, radar) -> {
      double udaljenostKm = GpsUdaljenostBrzina.udaljenostKm(radar.gpsSirina(), radar.gpsDuzina(),
          this.podaciVozila.gpsSirina(), this.podaciVozila.gpsDuzina());
      double udaljenostM = udaljenostKm * 1000;


      if (udaljenostM <= radar.maksUdaljenost()) {
        var komanda = new StringBuilder();
        String razmak = " ";
        komanda.append("VOZILO").append(razmak).append(this.podaciVozila.id()).append(razmak)
            .append(this.podaciVozila.vrijeme()).append(razmak).append(this.podaciVozila.brzina())
            .append(razmak).append(this.podaciVozila.gpsSirina()).append(razmak)
            .append(this.podaciVozila.gpsDuzina()).append("\n");

        MrezneOperacije.posaljiZahtjevPosluzitelju(radar.adresaRadara(), radar.mreznaVrataRadara(),
            komanda.toString());
      }
    });
  }

  /**
   * Preuzmi podatke vozila.
   * 
   * Preuzima podatke vozila iz zahtjeva.
   */
  public void preuzmiPodatkeVozila() {
    this.podaciVozila =
        new PodaciVozila(Integer.valueOf(this.poklapanjePosluziteljZaVozila.group("id")),
            Integer.valueOf(this.poklapanjePosluziteljZaVozila.group("broj")),
            Long.valueOf(this.poklapanjePosluziteljZaVozila.group("vrijeme")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("brzina")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("snaga")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("struja")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("visina")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("gpsBrzina")),
            Integer.valueOf(this.poklapanjePosluziteljZaVozila.group("tempVozila")),
            Integer.valueOf(this.poklapanjePosluziteljZaVozila.group("postotakBaterija")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("naponBaterija")),
            Integer.valueOf(this.poklapanjePosluziteljZaVozila.group("kapacitetBaterija")),
            Integer.valueOf(this.poklapanjePosluziteljZaVozila.group("tempBaterija")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("preostaloKm")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("ukupnoKm")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("gpsSirina")),
            Double.valueOf(this.poklapanjePosluziteljZaVozila.group("gpsDuzina")));
  }

}
