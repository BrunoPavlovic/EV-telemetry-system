package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci;

import jakarta.ws.rs.FormParam;

public class VoziloBean {
  @FormParam("id")
  private int id;

  @FormParam("broj")
  private int broj;

  @FormParam("vrijeme")
  private long vrijeme;

  @FormParam("brzina")
  private double brzina;

  @FormParam("snaga")
  private double snaga;

  @FormParam("struja")
  private double struja;

  @FormParam("visina")
  private double visina;

  @FormParam("gpsBrzina")
  private double gpsBrzina;

  @FormParam("tempVozila")
  private int tempVozila;

  @FormParam("postotakBaterija")
  private int postotakBaterija;

  @FormParam("naponBaterija")
  private double naponBaterija;

  @FormParam("kapacitetBaterija")
  private int kapacitetBaterija;

  @FormParam("tempBaterija")
  private int tempBaterija;

  @FormParam("preostaloKm")
  private double preostaloKm;

  @FormParam("ukupnoKm")
  private double ukupnoKm;

  @FormParam("gpsSirina")
  private double gpsSirina;

  @FormParam("gpsDuzina")
  private double gpsDuzina;

  public int getId() {
    return id;
  }

  public int getBroj() {
    return broj;
  }

  public long getVrijeme() {
    return vrijeme;
  }

  public double getBrzina() {
    return brzina;
  }

  public double getSnaga() {
    return snaga;
  }

  public double getStruja() {
    return struja;
  }

  public double getVisina() {
    return visina;
  }

  public double getGpsBrzina() {
    return gpsBrzina;
  }

  public int getTempVozila() {
    return tempVozila;
  }

  public int getPostotakBaterija() {
    return postotakBaterija;
  }

  public double getNaponBaterija() {
    return naponBaterija;
  }

  public int getKapacitetBaterija() {
    return kapacitetBaterija;
  }

  public int getTempBaterija() {
    return tempBaterija;
  }

  public double getPreostaloKm() {
    return preostaloKm;
  }

  public double getUkupnoKm() {
    return ukupnoKm;
  }

  public double getGpsSirina() {
    return gpsSirina;
  }

  public double getGpsDuzina() {
    return gpsDuzina;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setBroj(int broj) {
    this.broj = broj;
  }

  public void setVrijeme(long vrijeme) {
    this.vrijeme = vrijeme;
  }

  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  public void setSnaga(double snaga) {
    this.snaga = snaga;
  }

  public void setStruja(double struja) {
    this.struja = struja;
  }

  public void setVisina(double visina) {
    this.visina = visina;
  }

  public void setGpsBrzina(double gpsBrzina) {
    this.gpsBrzina = gpsBrzina;
  }

  public void setTempVozila(int tempVozila) {
    this.tempVozila = tempVozila;
  }

  public void setPostotakBaterija(int postotakBaterija) {
    this.postotakBaterija = postotakBaterija;
  }

  public void setNaponBaterija(double naponBaterija) {
    this.naponBaterija = naponBaterija;
  }

  public void setKapacitetBaterija(int kapacitetBaterija) {
    this.kapacitetBaterija = kapacitetBaterija;
  }

  public void setTempBaterija(int tempBaterija) {
    this.tempBaterija = tempBaterija;
  }

  public void setPreostaloKm(double preostaloKm) {
    this.preostaloKm = preostaloKm;
  }

  public void setUkupnoKm(double ukupnoKm) {
    this.ukupnoKm = ukupnoKm;
  }

  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }
}

