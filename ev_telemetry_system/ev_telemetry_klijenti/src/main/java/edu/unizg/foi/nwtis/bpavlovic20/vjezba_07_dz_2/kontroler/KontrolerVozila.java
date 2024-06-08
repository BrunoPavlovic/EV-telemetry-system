package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.model.RestKlijentVozila;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Odgovor;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Vozilo;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.VoziloBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Controller
@Path("vozila")
@RequestScoped
public class KontrolerVozila {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("vozila/index.jsp")
  public void pocetak() {}

  @POST
  @Path("pretrazivanjePracenihVoznji")
  @View("vozila/vozila.jsp")
  public void json_ppv(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentVozila k = new RestKlijentVozila();
    List<Vozilo> praceneVoznje = k.getPracenaVoznjaJSON_od_do(odVremena, doVremena);
    model.put("praceneVoznje", praceneVoznje);
  }

  @POST
  @Path("praceneVoznjeZaVozilo")
  @View("vozila/vozila.jsp")
  public void json_pvv(@FormParam("id") String id) {
    RestKlijentVozila k = new RestKlijentVozila();
    List<Vozilo> praceneVoznje = k.getPracenaVoznjaJSON_vozilo(id);
    model.put("praceneVoznje", praceneVoznje);
  }

  @POST
  @Path("pretrazivanjePracenihVoznjiZaVozilo")
  @View("vozila/vozila.jsp")
  public void json_ppv(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena, @FormParam("id") String id) {
    RestKlijentVozila k = new RestKlijentVozila();
    List<Vozilo> praceneVoznje = k.getPracenaVoznjaJSON_vozilo_od_do(id, odVremena, doVremena);
    model.put("praceneVoznje", praceneVoznje);
  }

  @POST
  @Path("start")
  @View("vozila/index.jsp")
  public void json_start(@FormParam("id") String id) {
    RestKlijentVozila k = new RestKlijentVozila();
    Odgovor o = k.getPracenaVoznjaJSON_start(id);
    model.put("start", o.getPoruka());
  }

  @POST
  @Path("stop")
  @View("vozila/index.jsp")
  public void json_stop(@FormParam("id") String id) {
    RestKlijentVozila k = new RestKlijentVozila();
    Odgovor o = k.getPracenaVoznjaJSON_stop(id);
    model.put("stop", o.getPoruka());
  }

  @POST
  @Path("dodajPracenuVoznju")
  @View("vozila/index.jsp")
  public void json_dpv(@BeanParam VoziloBean pracenaVoznjaBean) {
    RestKlijentVozila k = new RestKlijentVozila();

    Vozilo pracenaVoznja = new Vozilo(pracenaVoznjaBean.getId(), pracenaVoznjaBean.getBroj(),
        pracenaVoznjaBean.getVrijeme(), pracenaVoznjaBean.getBrzina(), pracenaVoznjaBean.getSnaga(),
        pracenaVoznjaBean.getStruja(), pracenaVoznjaBean.getVisina(),
        pracenaVoznjaBean.getGpsBrzina(), pracenaVoznjaBean.getTempVozila(),
        pracenaVoznjaBean.getPostotakBaterija(), pracenaVoznjaBean.getNaponBaterija(),
        pracenaVoznjaBean.getKapacitetBaterija(), pracenaVoznjaBean.getTempBaterija(),
        pracenaVoznjaBean.getPreostaloKm(), pracenaVoznjaBean.getUkupnoKm(),
        pracenaVoznjaBean.getGpsSirina(), pracenaVoznjaBean.getGpsDuzina());

    var odgovor = k.postPracenaVoznjaJSON(pracenaVoznja);
    if (odgovor) {
      model.put("poruka", "Uspješno dodana nova praćena vožnja");
    } else {
      model.put("poruka", "Nova praćena vožnja nije dodana ");
    }
  }

}
