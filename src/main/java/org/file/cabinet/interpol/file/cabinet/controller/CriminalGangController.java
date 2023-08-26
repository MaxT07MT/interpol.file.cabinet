package org.file.cabinet.interpol.file.cabinet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.service.CriminalGangService;
import org.file.cabinet.interpol.file.cabinet.service.OffenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/criminalGangs")
public class CriminalGangController {

  private final CriminalGangService criminalGangService;
  private final OffenderService offenderService;
  @Autowired
  public CriminalGangController(CriminalGangService criminalGangService,
      OffenderService offenderService) {
    this.criminalGangService = criminalGangService;
    this.offenderService = offenderService;
  }


  @GetMapping
  public String getAllCriminalGangs(Model model) {
    List<CriminalGang> criminalGangs = criminalGangService.findAll();
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/criminal-gang-list";
  }

  //Поиск действующих преступных группировок
  @GetMapping("/notArchived")
  public String getAllGangs(Model model, @RequestParam(required = false) String error) {
    List<CriminalGang> activeGangs = criminalGangService.findByGangArchivedFalseOrGangArchivedIsNull();
    model.addAttribute("activeGangs", activeGangs);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять действующую преступную группировку");
    }
    return "gangs/criminal-gang-list";

  }
  //Поиск преступных группировок помещенных в архив
  @GetMapping("/archive")
  public String getArchiveGangs(Model model, @RequestParam(required = false) String error){
    List<CriminalGang> archivedGangs = criminalGangService.findByGangArchivedTrue();
    model.addAttribute("archivedGangs", archivedGangs);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    return "gangs/criminal-gang-archive";
  }


  @GetMapping("/{id}")
  public String getCriminalGangById(@PathVariable long id, Model model) {
    CriminalGang criminalGang = criminalGangService.findById(id);
    model.addAttribute("criminalGang", criminalGang);
    return "gangs/criminal-gang-details";
  }

  @GetMapping("/create")
  public String createCriminalGangForm(Model model) {
    model.addAttribute("criminalGang", new CriminalGang());
    model.addAttribute("actionUrl", "/criminalGangs/create");
    return "gangs/criminal-gang-form";
  }

  @PostMapping("/create")
  public String createCriminalGang(@ModelAttribute CriminalGang criminalGang) {
    criminalGangService.create(criminalGang);
    return "redirect:/criminalGangs/notArchived";
  }

  @PostMapping("/{id}/uploadLogo")
  public String uploadCriminalGangLogo(@PathVariable long id, @RequestParam("logo") MultipartFile logoFile) throws IOException {
    CriminalGang criminalGang = criminalGangService.findById(id);

    if (!logoFile.isEmpty()) {
      criminalGang.setLogo(logoFile.getBytes());
      criminalGangService.update(criminalGang);
    }

    return "redirect:/criminalGangs/" + id;
  }
  @GetMapping("/{id}/logo")
  public ResponseEntity<byte[]> getCriminalGangLogo(@PathVariable long id) {
    CriminalGang criminalGang = criminalGangService.findById(id);

    if (criminalGang.getLogo() != null) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);

      return new ResponseEntity<>(criminalGang.getLogo(), headers, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }


  @GetMapping("/edit/{id}")
  public String editCriminalGangForm(@PathVariable long id, Model model) {
    CriminalGang criminalGang = criminalGangService.findById(id);
    model.addAttribute("criminalGang", criminalGang);
    model.addAttribute("actionUrl", "/criminalGangs/edit/" + id);
    return "gangs/criminal-gang-form";
  }

  @PostMapping("/edit/{id}")
  public String editCriminalGang(@PathVariable long id, @ModelAttribute CriminalGang criminalGang) {
    criminalGangService.update(criminalGang);
    return "redirect:/criminalGangs/{id}";
  }

  @GetMapping("/delete/{id}")
  public String deleteCriminalGang(@PathVariable long id) {
    CriminalGang gang = criminalGangService.findById(id);

    if (gang != null) {
      Set<Offender> offenders = gang.getOffenders();
      if (!offenders.isEmpty()) {
        for (Offender offender : offenders ) {
          offender.setGang(null);
          offenderService.updateOffender(offender);
        }
      }
      criminalGangService.deleteCriminalGang(id);
    }

    return "redirect:/criminalGangs";
  }




  @GetMapping("/{id}/offenders")
  public String showOffenders(@PathVariable long id, Model model) {
    CriminalGang criminalGang = criminalGangService.findById(id);
    List<Offender> offenders = new ArrayList<>(criminalGang.getOffenders());
    model.addAttribute("criminalGang", criminalGang);
    model.addAttribute("offender", offenders);
    return "gangs/criminal-gang-details";
  }

 // @GetMapping("/{id}/add-offender")
  //public String showAddOffenderForm(@PathVariable long id, Model model) {
   // CriminalGang criminalGang = criminalGangService.findById(id);
    //List<Offender> activeOffenders = offenderService.findByArchivedFalseOrArchivedIsNull();
    //model.addAttribute("criminalGang", criminalGang);
    //model.addAttribute("activeOffenders", activeOffenders);
   // return "gangs/add-offender-to-gang";
  //}


 // @PostMapping("/{id}/add-offender")
 // public String addOffenderToGang(@PathVariable long id, @RequestParam long offId) {
   // CriminalGang criminalGang = criminalGangService.findById(id);
    //Offender offender = offenderService.getOffenderById(offId);
    //criminalGang.getOffenders().add(offender);
    //criminalGangService.update(criminalGang);
    //return "redirect:/criminalGangs/" + id;
  //}
}




