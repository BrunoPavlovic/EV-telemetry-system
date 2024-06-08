package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara;

/**
 * Klasa RadnikZaRadare.
 */
public class RadnikZaRadare implements Runnable {

  /** Mrezna uticnica. */
  private Socket mreznaUticnica;

  /** Podaci radara. */
  private PodaciRadara podaciRadara;

  /** Posluzitelj radara. */
  private PosluziteljRadara posluziteljRadara;

  /** Podaci vozila. */
  private BrzoVozilo podaciVozila;

  /** Kolekcija sva brza vozila. */
  ConcurrentHashMap<Integer, BrzoVozilo> svaBrzaVozila;

  /** Predlozak brzine. */
  Pattern predlozakBrzine = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

  Pattern predlozakReset = Pattern.compile("^RADAR RESET$");
  Pattern predlozakRadara = Pattern.compile("^RADAR (?<id>\\d+)$");

  /** Poklapanje brzine. */
  Matcher poklapanjeBrzine;
  Matcher poklapanjeReset;
  Matcher poklapanjeRadara;

  /**
   * Instancira novi radnik za radare.
   *
   * @param mreznaUticnica
   * @param podaciRadara
   * @param svaBrzaVozila - kolekcija
   * @param posluziteljRadara
   */
  public RadnikZaRadare(Socket mreznaUticnica, PodaciRadara podaciRadara,
      ConcurrentHashMap<Integer, BrzoVozilo> svaBrzaVozila, PosluziteljRadara posluziteljRadara) {
    this.mreznaUticnica = mreznaUticnica;
    this.podaciRadara = podaciRadara;
    this.svaBrzaVozila = svaBrzaVozila;
    this.posluziteljRadara = posluziteljRadara;
  }

  /**
   * Run.
   * 
   * Metoda koja čita dolazne podatke, obrađuje ih i šalje odgovor klijentu.
   */
  @Override
  public void run() {
    try {
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
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
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
      return "ERROR 30 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaBrzine(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 30 Neispravna sintaksa komande.";
  }

  /**
   * Obrada zahtjeva brzine.
   * 
   * Preuzimaju se podaci od vozila, provjerava se vozi li vozilo brže od ograničenja te ako vozi
   * dovoljno dugo dobiva kaznu.
   *
   * @param zahtjev the zahtjev
   * @return OK ako je sve u redu, null ako nije pronađeno poklapanje te ERROR u ostalim
   *         slučajevima.
   */
  public String obradaZahtjevaBrzine(String zahtjev) {
    try {
      this.poklapanjeBrzine = this.predlozakBrzine.matcher(zahtjev);
      this.poklapanjeReset = this.predlozakReset.matcher(zahtjev);
      this.poklapanjeRadara = this.predlozakRadara.matcher(zahtjev);

      var status = poklapanjeBrzine.matches();
      if (status) {
        preuzmiPodatkeVozila();
        if (this.podaciVozila.brzina() > this.podaciRadara.maksBrzina()) {
          if (this.svaBrzaVozila.containsKey(this.podaciVozila.id())
              && this.svaBrzaVozila.get(this.podaciVozila.id()).status()) {
            long vrijemeBrzeVoznje = this.podaciVozila.vrijeme()
                - this.svaBrzaVozila.get(this.podaciVozila.id()).vrijeme();
            long maksTrajanjeMilisekunde = 1000 * this.podaciRadara.maksTrajanje();

            if (vrijemeBrzeVoznje > (2 * maksTrajanjeMilisekunde)) {
              ponistiPodatkeOVozilu();
            } else if (vrijemeBrzeVoznje > maksTrajanjeMilisekunde) {
              var odgovor = posaljiKaznu();
              if (odgovor == null) {
                return "ERROR 31 PosluziteljKazni nije aktivan";
              }

              ponistiPodatkeOVozilu();
            }
          } else {
            this.podaciVozila = this.podaciVozila.postaviStatus(true);
            this.svaBrzaVozila.put(this.podaciVozila.id(), this.podaciVozila);
          }
        } else {
          if (this.svaBrzaVozila.containsKey(this.podaciVozila.id())
              && this.svaBrzaVozila.get(this.podaciVozila.id()).status()) {
            ponistiPodatkeOVozilu();
          }
        }

        return "OK";
      } else if (poklapanjeReset.matches()) {
        return provjeriIspravnostPodataka();

      } else if (poklapanjeRadara.matches()) {

        return provjeraAktivnostiPosluziteljaKazni();
      }

      return null;
    } catch (Exception e) {
      return "ERROR 39 " + e.getMessage();
    }
  }

  private String provjeriIspravnostPodataka() {
    var komanda = "RADAR " + this.podaciRadara.id() + "\n";
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda);

    if (odgovor == "OK") {
      return "OK";
    }

    var o = posluziteljRadara.registrirajPosluzitelja();
    if (o) {
      return "OK";
    }

    return "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan";
  }

  private String provjeraAktivnostiPosluziteljaKazni() {
    var radarId = Integer.valueOf(this.poklapanjeRadara.group("id"));
    if (radarId != this.podaciRadara.id()) {
      return "ERROR 33 id ne odgovara identifikatoru radara.";
    }

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaKazne(),
        this.podaciRadara.mreznaVrataKazne(), "TEST\n");

    if (!odgovor.contentEquals("OK")) {
      return "ERROR 34 PosluziteljKazni nije aktivan";
    }

    return "OK";
  }

  /**
   * Posalji kaznu.
   * 
   * Šalje kaznu na poslužitelj kazni
   *
   * @return odgovor
   */
  public String posaljiKaznu() {
    var komanda = new StringBuilder();
    String razmak = " ";

    komanda.append("VOZILO").append(razmak).append(this.podaciVozila.id()).append(razmak)
        .append(this.svaBrzaVozila.get(this.podaciVozila.id()).vrijeme()).append(razmak)
        .append(this.podaciVozila.vrijeme()).append(razmak).append(this.podaciVozila.brzina())
        .append(razmak).append(this.podaciVozila.gpsSirina()).append(razmak)
        .append(this.podaciVozila.gpsDuzina()).append(razmak).append(this.podaciRadara.gpsSirina())
        .append(razmak).append(this.podaciRadara.gpsDuzina()).append("\n");

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaKazne(),
        this.podaciRadara.mreznaVrataKazne(), komanda.toString());

    return odgovor;
  }

  /**
   * Ponisti podatke O vozilu.
   * 
   * Ponista podatke o vozilu tako da se spremaju novi podaci sa statusom false.
   */
  public void ponistiPodatkeOVozilu() {
    this.podaciVozila = this.podaciVozila.postaviStatus(false);
    this.svaBrzaVozila.put(this.podaciVozila.id(), this.podaciVozila);
  }

  /**
   * Preuzmi podatke vozila.
   * 
   * Preuzima podatke za vozilo iz pristiglog zahtjeva.
   */
  public void preuzmiPodatkeVozila() {
    this.podaciVozila = new BrzoVozilo(Integer.valueOf(this.poklapanjeBrzine.group("id")), -1,
        Long.valueOf(this.poklapanjeBrzine.group("vrijeme")),
        Double.valueOf(this.poklapanjeBrzine.group("brzina")),
        Double.valueOf(this.poklapanjeBrzine.group("gpsSirina")),
        Double.valueOf(this.poklapanjeBrzine.group("gpsDuzina")), false);
  }

}
