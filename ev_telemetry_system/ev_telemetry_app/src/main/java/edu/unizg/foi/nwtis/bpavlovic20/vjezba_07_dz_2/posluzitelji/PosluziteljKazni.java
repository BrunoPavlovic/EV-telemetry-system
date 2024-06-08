package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.klijenti.RestKlijentKazne;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Kazna;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa PosluziteljKazni.
 */
public class PosluziteljKazni {

  /** Simple Date Format. */
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

  /** Mrezna vrata. */
  public int mreznaVrata;

  /** Predlozak kazne za dolazeće zahtjeve. */
  private Pattern predlozakKazna = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<gpsSirinaRadar>\\d+[.]\\d+) (?<gpsDuzinaRadar>\\d+[.]\\d+)$");

  /** Predlozak za nadolazeće zahtjeve kazni s vremenima od do. */
  private Pattern predlozakKaznaUnutarVremena =
      Pattern.compile("^VOZILO (?<id>\\d+) (?<vrijemeOd>\\d+) (?<vrijemeDo>\\d+)$");

  /** Predlozak za zahtjeve koji traže izračun statistika kazni. */
  private Pattern predlozakStatistikaKazni =
      Pattern.compile("^STATISTIKA (?<vrijemeOd>\\d+) (?<vrijemeDo>\\d+)$");

  private Pattern predlozakTest = Pattern.compile("^TEST$");

  /** Poklapanje kazna. */
  private Matcher poklapanjeKazna;

  /** Pklapanje kazna unutar vremena. */
  private Matcher poklapanjeKaznaUnutarVremena;

  /** Poklapanje statistika kazni. */
  private Matcher poklapanjeStatistikaKazni;

  private Matcher poklapanjeTest;

  /** Red sve kazne. */
  private volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();

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

    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    try {
      posluziteljKazni.preuzmiPostavke(args);

      posluziteljKazni.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println("ERROR 49 " + e.getMessage());
      return;
    }
  }

  /**
   * Pokreni posluzitelja.
   * 
   * otvara Socket na određenim mrežnim vratima, osluškuje i upravlja sa zahtjevima
   */
  public void pokreniPosluzitelja() {
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
      e.printStackTrace();
    }
  }

  /**
   * Obrada zahtjeva.
   *
   * @param zahtjev tipa String
   * @return odgovor - OK ili ERROR
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 40 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaKazna(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 40 Neispravna sintaksa komande.";
  }

  /**
   * Obrada zahtjeva kazna.
   * 
   * Obrada se odvija ovisno o poklapanju s predlošcima.
   *
   * @param zahtjev
   * @return vraća String ili ERROR
   */
  public String obradaZahtjevaKazna(String zahtjev) {
    try {
      this.poklapanjeKazna = this.predlozakKazna.matcher(zahtjev);
      this.poklapanjeKaznaUnutarVremena = this.predlozakKaznaUnutarVremena.matcher(zahtjev);
      this.poklapanjeStatistikaKazni = this.predlozakStatistikaKazni.matcher(zahtjev);
      this.poklapanjeTest = this.predlozakTest.matcher(zahtjev);

      var statusKazna = poklapanjeKazna.matches();
      if (statusKazna) {
        return upisiKaznu();

      } else if (poklapanjeKaznaUnutarVremena.matches()) {
        int id = Integer.valueOf(this.poklapanjeKaznaUnutarVremena.group("id"));
        long vrijemeOd = Long.valueOf(this.poklapanjeKaznaUnutarVremena.group("vrijemeOd"));
        long vrijemeDo = Long.valueOf(this.poklapanjeKaznaUnutarVremena.group("vrijemeDo"));

        return pronadiKaznu(id, vrijemeOd, vrijemeDo);

      } else if (poklapanjeStatistikaKazni.matches()) {
        long vrijemeOd = Long.valueOf(this.poklapanjeStatistikaKazni.group("vrijemeOd"));
        long vrijemeDo = Long.valueOf(this.poklapanjeStatistikaKazni.group("vrijemeDo"));

        return dajStatistikuKazni(vrijemeOd, vrijemeDo);

      } else if (poklapanjeTest.matches()) {
        return "OK";
      }
      return null;
    } catch (NumberFormatException e) {
      return "ERROR 49 " + e.getMessage();
    }
  }

  /**
   * Daj statistiku kazni.
   *
   * @param vrijemeOd
   * @param vrijemeDo
   * @return statistiku tipa String ili ERROR
   */
  public String dajStatistikuKazni(long vrijemeOd, long vrijemeDo) {
    ConcurrentHashMap<Integer, Integer> statistikaKazni = new ConcurrentHashMap<Integer, Integer>();
    try {
      for (PodaciKazne kazna : this.sveKazne) {
        if (kazna.vrijemePocetak() >= vrijemeOd && kazna.vrijemeKraj() <= vrijemeDo) {
          statistikaKazni.put(kazna.id(), statistikaKazni.getOrDefault(kazna.id(), 0) + 1);
        }
      }

      if (statistikaKazni.isEmpty()) {
        return "ERROR 49 Ne postoje kazne unutar zadanog vremena.";
      }

      StringBuilder rezultat = new StringBuilder();
      rezultat.append("OK");
      String razmak = " ";
      statistikaKazni.forEach((ključ, vrijednost) -> {
        rezultat.append(razmak);
        rezultat.append(ključ + " " + vrijednost + ";");
      });

      return rezultat.toString();
    } catch (Exception e) {
      return "ERROR 49 Došlo je do pogreške pri traženju statistike kazni.";
    }
  }

  /**
   * Pronadi kaznu.
   *
   * @param id - id vozila
   * @param vrijemeOd
   * @param vrijemeDo
   * @return najsvjeziju (zadnju upisanu) kaznu za vozilo ili ERROR
   */
  public String pronadiKaznu(int id, long vrijemeOd, long vrijemeDo) {
    List<PodaciKazne> pronadeneKazne = new ArrayList<PodaciKazne>();

    for (PodaciKazne kazna : this.sveKazne) {
      if (kazna.id() == id) {
        pronadeneKazne.add(kazna);
      }
    }

    if (pronadeneKazne.isEmpty())
      return "ERROR 49 Ne postoji kazna s ID: " + id + " ni za jedno e-vozilo.";

    for (int i = pronadeneKazne.size(); i-- > 0;) {
      PodaciKazne kazna = pronadeneKazne.get(i);
      if (kazna.vrijemePocetak() >= vrijemeOd && kazna.vrijemeKraj() <= vrijemeDo) {
        return "OK " + kazna.vrijemeKraj() + " " + kazna.brzina() + " " + kazna.gpsSirinaRadar()
            + " " + kazna.gpsDuzinaRadar();
      }
    }

    return "ERROR 41 e-vozilo s ID: " + id + " nema kaznu u zadanom vremenu.";
  }

  /**
   * Upisi kaznu.
   *
   * @return potvrda o upisu kazne ili ERROR
   */
  public String upisiKaznu() {
    try {
      var kazna = new PodaciKazne(Integer.valueOf(this.poklapanjeKazna.group("id")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")),
          Double.valueOf(this.poklapanjeKazna.group("brzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

      this.sveKazne.add(kazna);
      System.out.println("Id: " + kazna.id() + " Vrijeme od: " + sdf.format(kazna.vrijemePocetak())
          + "  Vrijeme do: " + sdf.format(kazna.vrijemeKraj()) + " Brzina: " + kazna.brzina()
          + " GPS: " + kazna.gpsSirina() + ", " + kazna.gpsDuzina());



      RestKlijentKazne rkk = new RestKlijentKazne();
      var kaznaZaRest =
          new Kazna(kazna.id(), kazna.vrijemePocetak(), kazna.vrijemeKraj(), kazna.brzina(),
              kazna.gpsSirina(), kazna.gpsDuzina(), kazna.gpsSirinaRadar(), kazna.gpsDuzinaRadar());
      var o = rkk.postKaznaJSON(kaznaZaRest);
      if (!o) {
        return "ERROR 42 kazna nije uspjesno poslana na RESTful web servis (POST metoda nije uspješno obavljena).";
      }



      return "OK";
    } catch (NumberFormatException e) {
      return "ERROR 49 Ne mogu se upisati podaci za e-vozilo u evidenciju kazni.";
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

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
  }
}
