package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.MrezneOperacije;

/**
 * Klasa PosluziteljZaRegistracijuRadara.
 */
public class PosluziteljZaRegistracijuRadara implements Runnable {

  /** Mrezna vrata. */
  private int mreznaVrata;

  /** Centralni sustav. */
  private CentralniSustav centralniSustav;

  /** Predlozak registracije radara. */
  private Pattern predlozakRegistracijeRadara = Pattern.compile(
      "^RADAR (?<id>\\d+) (?<adresa>.+) (?<mreznaVrata>\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<maksUdaljenost>-?\\d+)$");

  /** Predlozak brisanja radara. */
  private Pattern predlozakBrisanjaRadara = Pattern.compile("^RADAR OBRIŠI (?<id>\\d+)$");

  /** Predlozak brisanja svih radara. */
  private Pattern predlozakBrisanjaSvihRadara = Pattern.compile("^RADAR OBRIŠI SVE$");

  private Pattern predlozakDohvacanjaRadara = Pattern.compile("^RADAR (?<id>\\d+)$");
  private Pattern predlozakDohvatiSve = Pattern.compile("^RADAR SVI$");
  private Pattern predlozakReset = Pattern.compile("^RADAR RESET$");
  /** Poklapanje registracije radara. */
  private Matcher poklapanjeRegistracijeRadara;

  /** Poklapanje brisanja radara. */
  private Matcher poklapanjeBrisanjaRadara;

  /** Poklapanje brisanja svih radara. */
  private Matcher poklapanjeBrisanjaSvihRadara;
  private Matcher poklapanjeDohvacanjaRadara;
  private Matcher poklapanjeDohvatiSve;
  private Matcher poklapanjeReset;

  /**
   * Instancira novi poslužitelj za registraciju radara.
   *
   * @param mreznaVrata - proslijeđena mrežna vrata
   * @param centralniSustav
   */
  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }


  /**
   * Run metoda.
   * 
   * Osluškuje mrežne zahtjeve na mrežnim vratima i obrađuje ih.
   */
  @Override
  public void run() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
        OutputStream out = mreznaUticnica.getOutputStream();
        PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
        var redak = citac.readLine();

        mreznaUticnica.shutdownInput();
        pisac.println(obradaZahtjeva(redak));

        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
      System.out.println("ERROR 19 " + e.getMessage());
    }
  }


  /**
   * Obrada zahtjeva.
   *
   * @param zahtjev tipa String
   * @return odgovor ili ERROR
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaRegistracijeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 10 Neispravna sintaksa komande.";
  }

  /**
   * Obrada zahtjeva registracije radara.
   * 
   * Ovisno o poklapanju zahtjeva događa se registracija radara, brisanje radara ili brisanje svih
   * radara.
   *
   * @param zahtjev tipa String
   * @return OK ako je sve u redu, null ako zahtjev nema poklapanja, u ostalim slučajevima ERROR.
   */
  public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
    try {
      this.poklapanjeRegistracijeRadara = this.predlozakRegistracijeRadara.matcher(zahtjev);
      this.poklapanjeBrisanjaRadara = this.predlozakBrisanjaRadara.matcher(zahtjev);
      this.poklapanjeBrisanjaSvihRadara = this.predlozakBrisanjaSvihRadara.matcher(zahtjev);
      this.poklapanjeDohvacanjaRadara = this.predlozakDohvacanjaRadara.matcher(zahtjev);
      this.poklapanjeDohvatiSve = this.predlozakDohvatiSve.matcher(zahtjev);
      this.poklapanjeReset = this.predlozakReset.matcher(zahtjev);

      if (poklapanjeRegistracijeRadara.matches()) {
        var radarId = Integer.valueOf(this.poklapanjeRegistracijeRadara.group("id"));
        if (this.centralniSustav.sviRadari.containsKey(radarId)) {
          return "ERROR 11 Radar s identifikatorom " + radarId + " već postoji.";
        }

        var radar = new PodaciRadara(radarId, this.poklapanjeRegistracijeRadara.group("adresa"),
            Integer.valueOf(this.poklapanjeRegistracijeRadara.group("mreznaVrata")), -1, -1,
            Integer.valueOf(this.poklapanjeRegistracijeRadara.group("maksUdaljenost")), null, -1,
            null, -1, null, Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsSirina")),
            Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsDuzina")));

        this.centralniSustav.sviRadari.put(radar.id(), radar);
        return "OK";

      } else if (poklapanjeBrisanjaRadara.matches()) {
        return obrisiRadar();

      } else if (poklapanjeBrisanjaSvihRadara.matches()) {
        this.centralniSustav.sviRadari.clear();
        return "OK";

      } else if (poklapanjeDohvacanjaRadara.matches()) {
        var radarId = Integer.valueOf(this.poklapanjeDohvacanjaRadara.group("id"));
        if (this.centralniSustav.sviRadari.containsKey(radarId)) {
          return "OK";
        }

        return "ERROR 12 Radar s identifikatorom " + radarId + " ne postoji. ";
      } else if (poklapanjeDohvatiSve.matches()) {
        return dohvatiSveRadare();

      } else if (poklapanjeReset.matches()) {
        return provjeriAktivnost();
      }

      return null;
    } catch (NumberFormatException e) {
      return "ERROR 19 Pogreška u radu " + e.getMessage();
    }
  }


  private String dohvatiSveRadare() {
    var podaci = new StringBuilder();
    podaci.append("OK {");
    String razmak = " ";

    AtomicInteger brojac = new AtomicInteger();
    this.centralniSustav.sviRadari.forEach((ključ, radar) -> {
      if (brojac.get() != 0) {
        podaci.append(", ");
      }

      podaci.append("[").append(radar.id()).append(razmak).append(radar.adresaRadara())
          .append(razmak).append(radar.mreznaVrataRadara()).append(razmak).append(radar.gpsSirina())
          .append(razmak).append(radar.gpsDuzina()).append(razmak).append(radar.maksUdaljenost())
          .append("]");

      brojac.incrementAndGet();
    });

    podaci.append("}");

    return podaci.toString();
  }


  private String obrisiRadar() {
    var radarId = Integer.valueOf(this.poklapanjeBrisanjaRadara.group("id"));
    if (this.centralniSustav.sviRadari.containsKey(radarId)) {
      this.centralniSustav.sviRadari.remove(radarId);
      return "OK";
    }

    return "ERROR 12 Radar s identifikatorom " + radarId + " ne postoji. ";
  }


  private String provjeriAktivnost() {
    var komanda = new StringBuilder();

    int brojRadara = this.centralniSustav.sviRadari.size();
    this.centralniSustav.sviRadari.forEach((kljuc, radar) -> {
      komanda.append("RADAR").append(" ").append(radar.id()).append("\n");

      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(radar.adresaRadara(),
          radar.mreznaVrataRadara(), komanda.toString());

      if (odgovor.contains("ERROR 34")) {
        this.centralniSustav.sviRadari.remove(kljuc);
      }

      komanda.setLength(0);
    });

    int obrisaniRadari = brojRadara - this.centralniSustav.sviRadari.size();

    return "OK " + brojRadara + " " + obrisaniRadari;

  }
}
