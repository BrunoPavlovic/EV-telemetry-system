package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.kontroler;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.model.RestKlijentRadari;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Odgovor;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Radar;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Controller
@Path("radari")
@RequestScoped
public class KontrolerRadari {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("radari/index.jsp")
  public void pocetak() {}

  @GET
  @Path("ispisRadara")
  @View("radari/radari.jsp")
  public void ispisRadara() {
    RestKlijentRadari k = new RestKlijentRadari();
    List<Radar> radari = k.getRadariJSON();
    model.put("radari", radari);
  }

  @POST
  @Path("podaciZaRadar")
  @View("radari/radari.jsp")
  public void dohvatiRadar(@FormParam("id") String id) {
    RestKlijentRadari k = new RestKlijentRadari();
    Radar radar = k.getRadarJSON(id);
    List<Radar> radari = new ArrayList<Radar>();
    radari.add(radar);
    model.put("radari", radari);
  }

  @POST
  @Path("reset")
  @View("radari/index.jsp")
  public void resetRadara() {
    RestKlijentRadari k = new RestKlijentRadari();
    Odgovor o = k.getResetRadarJSON();
    model.put("porukaReset", o.getPoruka());
    System.out.println("porukaReset: " + o.getPoruka());
  }

  @POST
  @Path("provjeriRadar")
  @View("radari/index.jsp")
  public void provjeraRadara(@FormParam("id") String id) {
    RestKlijentRadari k = new RestKlijentRadari();
    Odgovor o = k.getProvjeriRadarJSON(id);
    model.put("porukaProvjera", o.getPoruka());
    System.out.println("provjera: " + o.getPoruka());
  }

  @POST
  @Path("obrisiSve")
  @View("radari/index.jsp")
  public void brisanjeSvihRadara() {
    RestKlijentRadari k = new RestKlijentRadari();
    Odgovor o = k.obrisiSveRadare();
    model.put("porukaBrisanjeSvih", o.getPoruka());
    System.out.println("obrisi sve: " + o.getPoruka());
  }

  @POST
  @Path("obrisiRadar")
  @View("radari/index.jsp")
  public void obrisiRadar(@FormParam("id") String id) {
    RestKlijentRadari k = new RestKlijentRadari();
    Odgovor o = k.obrisiRadar(id);
    model.put("porukaObrisiRadar", o.getPoruka());
    System.out.println("poruka obrisi radar: " + o.getPoruka());
  }

}
