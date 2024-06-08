package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci;

public class Odgovor {
  private String status;
  private String poruka;

  public Odgovor() {}

  public Odgovor(String status, String poruka) {
    super();
    this.status = status;
    this.poruka = poruka;
  }

  public String getStatus() {
    return status;
  }

  public String getPoruka() {
    return poruka;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setPoruka(String poruka) {
    this.poruka = poruka;
  }


}
