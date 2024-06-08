/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.kontroler;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Kazna;
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
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("kazne")
@RequestScoped
public class Kontroler {

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("kazne/index.jsp")
  public void pocetak() {}

  @GET
  @Path("ispisKazni")
  @View("kazne/kazne.jsp")
  public void json() {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON();
    model.put("kazne", kazne);
  }

  @POST
  @Path("pretrazivanjeKazni")
  @View("kazne/kazne.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_od_do(odVremena, doVremena);
    model.put("kazne", kazne);
  }

  @POST
  @Path("pretrazivanjeKazniZaVozilo")
  @View("kazne/kazne.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena, @FormParam("id") String id) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo_od_do(id, odVremena, doVremena);
    model.put("kazne", kazne);
  }

  @GET
  @Path("ispisKazniZaVozilo")
  @View("kazne/kazne.jsp")
  public void json_ik(@QueryParam("id") String id) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo(id);
    model.put("kazne", kazne);
  }


  @POST
  @Path("ispisKazniZaVozilo")
  @View("kazne/kazne.jsp")
  public void json_pkv(@FormParam("id") String id) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo(id);
    model.put("kazne", kazne);
  }

  @POST
  @Path("ispisKazniRedniBroj")
  @View("kazne/kazne.jsp")
  public void json_pkrb(@FormParam("rb") String rb) {
    RestKlijentKazne k = new RestKlijentKazne();
    Kazna kazna = k.getKaznaJSON_rb(rb);

    List<Kazna> kazne = new ArrayList<Kazna>();
    kazne.add(kazna);
    model.put("kazne", kazne);

  }

}
