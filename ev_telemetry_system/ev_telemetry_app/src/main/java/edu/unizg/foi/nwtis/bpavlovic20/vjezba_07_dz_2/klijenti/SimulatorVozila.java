package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.klijenti;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa SimulatorVozila.
 */
public class SimulatorVozila {

  /** Adresa vozila. */
  private String adresaVozila;

  /** Mrezna vrata vozila. */
  private int mreznaVrataVozila;

  /** Trajanje sek. */
  private int trajanjeSek;

  /** Trajanje pauze. */
  private int trajanjePauze;

  static Charset cs = Charset.forName("UTF-8");

  /**
   * Main metoda.
   *
   * @param args - argumenti dani prilikom pokretanja klase
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Broj argumenata nije 3.");
      return;
    }

    SimulatorVozila sv = new SimulatorVozila();
    try {
      sv.preuzmiPostavke(args);
      sv.pokreniKlijenta(args);
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokreni klijenta .
   * 
   * Metoda služi za spajanje na otvoreni asinkroni kanal, čita podatke iz csv datoteke i šalje ih
   * poslužitelju
   *
   * @param args - argumenti dani prilikom pokretanja klase
   */
  public void pokreniKlijenta(String[] args) {
    try {
      AsynchronousSocketChannel klijent = AsynchronousSocketChannel.open();
      SocketAddress serverAddr = new InetSocketAddress(this.adresaVozila, this.mreznaVrataVozila);
      Future<Void> rezultat = klijent.connect(serverAddr);
      rezultat.get();

      var citac = Files.newBufferedReader(Path.of(args[1]), cs);
      String redak;
      int brojRetka = 0;
      Long prethodnoVrijeme = 0L, vrijemeSpavanja;
      while ((redak = citac.readLine()) != null) {
        if (brojRetka != 0) {
          String[] podaci = redak.split(",");

          if (brojRetka > 1) {
            vrijemeSpavanja = Long.valueOf(podaci[0]) - prethodnoVrijeme;
            Thread.sleep(Long.valueOf((long) (vrijemeSpavanja * (this.trajanjeSek / 1000.0))));
          }

          prethodnoVrijeme = Long.valueOf(podaci[0]);
          String komanda = pripremiKomandu(podaci, Integer.valueOf(args[2]), brojRetka);

          byte[] komandaBajtovi = new String(komanda).getBytes();
          ByteBuffer buffer = ByteBuffer.wrap(komandaBajtovi);

          var pisacBuffer = klijent.write(buffer);
          pisacBuffer.get();

          buffer.clear();
          Thread.sleep(this.trajanjePauze);
        }

        brojRetka++;
      }

      klijent.close();
      citac.close();
    } catch (ExecutionException | InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Pripremi komandu - priprema komandu za slanje na server.
   *
   * @param podaci - podaci učitani iz csv-a i razdvojeni po stupcima
   * @param id - id vozila
   * @param brojRetka
   * @return komanda kao tip podataka String
   */
  public String pripremiKomandu(String[] podaci, int id, int brojRetka) {
    var komanda = new StringBuilder();
    var razmak = " ";
    komanda.append("VOZILO").append(razmak).append(id).append(razmak).append(brojRetka)
        .append(razmak).append(podaci[0]).append(razmak).append(podaci[1]).append(razmak)
        .append(podaci[2]).append(razmak).append(podaci[3]).append(razmak).append(podaci[4])
        .append(razmak).append(podaci[5]).append(razmak).append(podaci[6]).append(razmak)
        .append(podaci[7]).append(razmak).append(podaci[8]).append(razmak).append(podaci[9])
        .append(razmak).append(podaci[10]).append(razmak).append(podaci[11]).append(razmak)
        .append(podaci[12]).append(razmak).append(podaci[13]).append(razmak).append(podaci[14])
        .append("\n");

    return komanda.toString();

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

    this.adresaVozila = konfig.dajPostavku("adresaVozila");
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.trajanjeSek = Integer.valueOf(konfig.dajPostavku("trajanjeSek"));
    this.trajanjePauze = Integer.valueOf(konfig.dajPostavku("trajanjePauze"));
  }

}
