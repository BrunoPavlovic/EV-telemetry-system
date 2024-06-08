package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaVozila;

/**
 * Klasa PosluziteljZaVozila.
 */
public class PosluziteljZaVozila implements Runnable {

  /** Mrezna vrata. */
  private int mreznaVrata;

  /** Centralni sustav. */
  private CentralniSustav centralniSustav;

  /** Tvornica dretvi. */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /** Izvršitelj. */
  private static ExecutorService executor = null;

  /**
   * Instancira novi posluzitelj za vozila.
   *
   * @param mreznaVrata - proslijeđena mrežna vrata
   * @param centralniSustav
   */
  public PosluziteljZaVozila(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }


  /**
   * Run.
   * 
   * Pokreće poslužitelja koji osluškuje mrežna vrata te za svakog klijenta kreira novu dretvu.
   */
  @Override
  public void run() {
    try {
      final AsynchronousServerSocketChannel server =
          AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(this.mreznaVrata));
      executor = Executors.newThreadPerTaskExecutor(tvornicaDretvi);
      boolean kraj = false;
      while (!kraj) {
        Future<AsynchronousSocketChannel> kanalZaPrihvacanje = server.accept();
        AsynchronousSocketChannel klijent = kanalZaPrihvacanje.get();

        var radnik =
            new RadnikZaVozila(klijent, this.centralniSustav.sviRadari, this.centralniSustav);

        executor.execute(radnik);
      }
    } catch (NumberFormatException | InterruptedException | ExecutionException | IOException e) {
      e.printStackTrace();
    }
  }
}
