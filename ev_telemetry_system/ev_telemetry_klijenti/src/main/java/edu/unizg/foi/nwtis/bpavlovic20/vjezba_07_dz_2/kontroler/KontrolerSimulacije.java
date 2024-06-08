package edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.model.RestKlijentSimulacije;
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
@Path("simulacije")
@RequestScoped
public class KontrolerSimulacije {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("simulacije/index.jsp")
  public void pocetak() {}

  @POST
  @Path("pretrazivanjeVoznji")
  @View("simulacije/voznje.jsp")
  public void json_pv(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentSimulacije k = new RestKlijentSimulacije();
    List<Vozilo> voznje = k.getSimulacijeJSON_od_do(odVremena, doVremena);
    model.put("voznje", voznje);
  }

  @POST
  @Path("voznjeZaVozilo")
  @View("simulacije/voznje.jsp")
  public void json_pv(@FormParam("id") String id) {
    RestKlijentSimulacije k = new RestKlijentSimulacije();
    List<Vozilo> voznje = k.getSimulacijeJSON_vozilo(id);
    model.put("voznje", voznje);
  }

  @POST
  @Path("pretrazivanjeVoznjiZaVozilo")
  @View("simulacije/voznje.jsp")
  public void json_pvv(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena, @FormParam("id") String id) {
    RestKlijentSimulacije k = new RestKlijentSimulacije();
    List<Vozilo> voznje = k.getSimulacijeJSON_vozilo_od_do(id, odVremena, doVremena);
    model.put("voznje", voznje);
  }

  @POST
  @Path("dodajVoznju")
  @View("simulacije/index.jsp")
  public void json_dv(@BeanParam VoziloBean voznjaBean) {
    RestKlijentSimulacije k = new RestKlijentSimulacije();

    Vozilo voznja = new Vozilo(voznjaBean.getId(), voznjaBean.getBroj(), voznjaBean.getVrijeme(),
        voznjaBean.getBrzina(), voznjaBean.getSnaga(), voznjaBean.getStruja(),
        voznjaBean.getVisina(), voznjaBean.getGpsBrzina(), voznjaBean.getTempVozila(),
        voznjaBean.getPostotakBaterija(), voznjaBean.getNaponBaterija(),
        voznjaBean.getKapacitetBaterija(), voznjaBean.getTempBaterija(),
        voznjaBean.getPreostaloKm(), voznjaBean.getUkupnoKm(), voznjaBean.getGpsSirina(),
        voznjaBean.getGpsDuzina());

    var odgovor = k.postSimulacijeJSON(voznja);
    if (odgovor) {
      model.put("poruka", "Uspješno dodana nova vožnja");
    } else {
      model.put("poruka", "Nova vožnja nije dodana ");
    }
  }

}
