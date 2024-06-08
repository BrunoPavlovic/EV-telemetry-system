package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa PraceneVoznjeDAO za rad s tablicom PracenjeVoznje
 *
 */
public class PraceneVoznjeDAO {

  /** Veza na bazu podataka. */
  private Connection vezaBP;

  /**
   * Inicijalizira novu PraceneVoznjeDAO.
   *
   * @param vezaBP veza na bazu podataka
   */
  public PraceneVoznjeDAO(Connection vezaBP) {
    super();
    this.vezaBP = vezaBP;
  }

  /**
   * Dohvati pracene voznje.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return lista pracenih voznji
   */
  public List<Vozilo> dohvatiPracenjeVoznje(long odVremena, long doVremena) {
    String upit = "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,"
        + "        postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm,"
        + "        gpsSirina, gpsDuzina "
        + "FROM pracenevoznje WHERE vrijeme >= ? AND vrijeme <= ?";

    List<Vozilo> praceneVoznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setLong(1, odVremena);
      s.setLong(2, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var vozilo = kreirajObjektVozilo(rs);
        praceneVoznje.add(vozilo);
      }
    } catch (SQLException ex) {
      Logger.getLogger(PraceneVoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return praceneVoznje;
  }


  /**
   * Dohvati pracene voznje vozila.
   *
   * @param id id vozila
   * @return lista pracenih voznji
   */
  public List<Vozilo> dohvatiPraceneVoznjeVozila(int id) {
    String upit = "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,"
        + "postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm,"
        + "gpsSirina, gpsDuzina FROM pracenevoznje WHERE id = ?";

    List<Vozilo> praceneVoznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var vozilo = kreirajObjektVozilo(rs);
        praceneVoznje.add(vozilo);
      }
    } catch (SQLException ex) {
      Logger.getLogger(PraceneVoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return praceneVoznje;
  }


  /**
   * Dohvati pracene voznje vozila.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return lista pracenih voznji
   */
  public List<Vozilo> dohvatiPraceneVoznjeVozila(int id, long odVremena, long doVremena) {
    String upit = "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,"
        + "postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm,"
        + "gpsSirina, gpsDuzina FROM pracenevoznje WHERE id = ? AND vrijeme >= ? AND vrijeme <= ?";

    List<Vozilo> praceneVoznje = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      s.setLong(2, odVremena);
      s.setLong(3, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var vozilo = kreirajObjektVozilo(rs);
        praceneVoznje.add(vozilo);
      }
    } catch (SQLException ex) {
      Logger.getLogger(PraceneVoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return praceneVoznje;
  }

  /**
   * Dodaj pracenje voznje za e-vozilo
   * 
   * @param vozilo
   * @return true, ako je dodavanje uspjesno
   */
  public boolean dodajPracenuVoznji(Vozilo vozilo) {
    String upit =
        "INSERT INTO pracenevoznje(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, vozilo.getId());
      s.setInt(2, vozilo.getBroj());
      s.setLong(3, vozilo.getVrijeme());
      s.setDouble(4, vozilo.getBrzina());
      s.setDouble(5, vozilo.getSnaga());
      s.setDouble(6, vozilo.getStruja());
      s.setDouble(7, vozilo.getVisina());
      s.setDouble(8, vozilo.getGpsBrzina());
      s.setInt(9, vozilo.getTempVozila());
      s.setInt(10, vozilo.getPostotakBaterija());
      s.setDouble(11, vozilo.getNaponBaterija());
      s.setInt(12, vozilo.getKapacitetBaterija());
      s.setInt(13, vozilo.getTempBaterija());
      s.setDouble(14, vozilo.getPreostaloKm());
      s.setDouble(15, vozilo.getUkupnoKm());
      s.setDouble(16, vozilo.getGpsSirina());
      s.setDouble(17, vozilo.getGpsDuzina());

      int brojAzuriranja = s.executeUpdate();
      return brojAzuriranja == 1;

    } catch (Exception ex) {
      Logger.getLogger(PraceneVoznjeDAO.class.getName()).log(Level.SEVERE, null, ex);
    }

    return false;
  }


  /**
   * Kreiraj objekt vozilo.
   *
   * @param rs skup rezultata SQL upisa
   * @return vozilo
   * @throws SQLException SQL iznimka
   */
  private Vozilo kreirajObjektVozilo(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    int broj = rs.getInt("broj");
    long vrijeme = rs.getLong("vrijeme");
    double brzina = rs.getDouble("brzina");
    double snaga = rs.getDouble("snaga");
    double struja = rs.getDouble("struja");
    double visina = rs.getDouble("visina");
    double gpsBrzina = rs.getDouble("gpsBrzina");
    int tempVozila = rs.getInt("tempVozila");
    int postotakBaterija = rs.getInt("postotakBaterija");
    double naponBaterija = rs.getDouble("naponBaterija");
    int kapacitetBaterija = rs.getInt("kapacitetBaterija");
    int tempBaterija = rs.getInt("tempBaterija");
    double preostaloKm = rs.getDouble("preostaloKm");
    double ukupnoKm = rs.getDouble("ukupnoKm");
    double gpsSirina = rs.getDouble("gpsSirina");
    double gpsDuzina = rs.getDouble("gpsDuzina");

    Vozilo v = new Vozilo(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,
        postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm,
        gpsSirina, gpsDuzina);
    return v;
  }
}
