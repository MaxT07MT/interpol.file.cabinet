package org.file.cabinet.interpol.file.cabinet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.service.CrimeService;
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
@RequestMapping("/criminalGang")
public class CriminalGangController {

  private final CriminalGangService criminalGangService;
  private final OffenderService offenderService;
  private final CrimeService crimeService;
  @Autowired
  public CriminalGangController(CriminalGangService criminalGangService,
      OffenderService offenderService, CrimeService crimeService) {
    this.criminalGangService = criminalGangService;
    this.offenderService = offenderService;
    this.crimeService = crimeService;
  }


  @GetMapping
  public String getAllCriminalGangs(Model model) {
    List<CriminalGang> criminalGangs = criminalGangService.getAll();
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/criminal-gang-list";
  }

  //Поиск действующих преступных группировок
  @GetMapping("/notArchived")
  public String getAllGangs(Model model, @RequestParam(required = false) String error) {
    List<CriminalGang> criminalGangs = criminalGangService.getByGangArchivedFalseOrGangArchivedIsNull();
    model.addAttribute("criminalGangs", criminalGangs);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять действующую преступную группировку");
    }
    return "gangs/search-criminal-gangs-page";

  }
  //Поиск преступных группировок помещенных в архив
  @GetMapping("/archive")
  public String getArchivedGangs(Model model, @RequestParam(required = false) String error){
    List<CriminalGang> criminalGangs = criminalGangService.getByGangArchivedTrue();
    model.addAttribute("criminalGangs", criminalGangs);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    return "gangs/search-criminal-gangs-page";
  }


  @GetMapping("/{id}")
  public String getCriminalGangById(@PathVariable long id, Model model) {
    CriminalGang criminalGang = criminalGangService.getById(id);
    Set<Offender> offenders = criminalGang.getOffenders();
    Set<Crime> crimes = new HashSet<>();
    for (Offender offender : offenders) {
      crimes.addAll(offender.getCrimes());
    }
    model.addAttribute("criminalGang", criminalGang);
    model.addAttribute("crimes", crimes);

    return "gangs/criminal-gang-details";
  }



  @GetMapping("/create")
  public String createCriminalGangForm(Model model) {
    model.addAttribute("criminalGang", new CriminalGang());
    model.addAttribute("actionUrl", "/criminalGang/create");
    return "gangs/criminal-gang-create";
  }

  @PostMapping("/create")
  public String createCriminalGang(@ModelAttribute CriminalGang criminalGang) {
    criminalGangService.createCriminalGang(criminalGang);
    return "redirect:/criminalGang";
  }

  @PostMapping("/{id}/uploadLogo")
  public String uploadCriminalGangLogo(@PathVariable long id, @RequestParam("logo") MultipartFile logoFile) throws IOException {
    CriminalGang criminalGang = criminalGangService.getById(id);

    if (!logoFile.isEmpty()) {
      criminalGang.setLogo(logoFile.getBytes());
      criminalGangService.updateCriminalGang(criminalGang);
    }

    return "redirect:/criminalGang/edit/{id}";
  }
  @GetMapping("/{id}/logo")
  public ResponseEntity<byte[]> getCriminalGangLogo(@PathVariable long id) {
    CriminalGang criminalGang = criminalGangService.getById(id);

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
    CriminalGang criminalGang = criminalGangService.getById(id);
    model.addAttribute("criminalGang", criminalGang);
    model.addAttribute("actionUrl", "/criminalGang/edit/" + id);
    return "gangs/criminal-gang-edit";
  }

  @PostMapping("/edit/{id}")
  public String editCriminalGang(@PathVariable long id, @ModelAttribute CriminalGang criminalGang) {
    criminalGangService.updateCriminalGang(criminalGang);
    return "redirect:/criminalGang/{id}";
  }

  @GetMapping("/delete/{id}")
  public String deleteCriminalGang(@PathVariable long id) {
    CriminalGang gang = criminalGangService.getById(id);

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

    return "redirect:/criminalGang";
  }

  @GetMapping("/{id}/offender")
  public String showOffenders(@PathVariable long id, Model model) {
    CriminalGang criminalGang = criminalGangService.getById(id);
    List<Offender> offenders = new ArrayList<>(criminalGang.getOffenders());
    model.addAttribute("criminalGang", criminalGang);
    model.addAttribute("offender", offenders);
    return "gangs/criminal-gang-details";
  }
  @GetMapping("/searchPage")
  public String searchCriminalGangsPage(Model model){
    List<CriminalGang> criminalGangs = criminalGangService.getAll();
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/search-criminal-gangs-page";
  }
  @GetMapping("/searchByName")
  public String searchGangsByStartingLetters(
      @RequestParam("startingLetters") String name,
      Model model) {
    List<CriminalGang> criminalGangs = criminalGangService.getByNameStartsWithIgnoreCase(name);
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/search-criminal-gangs-page";
  }
  @PostMapping("/{id}/toggleArchive")
  public String toggleArchive(@PathVariable long id) {
    CriminalGang criminalGang = criminalGangService.getById(id);
    if (criminalGang != null) {
      Boolean archived = criminalGang.getGangArchived();
      if (archived == null) {
        criminalGang.setGangArchived(false);
      } else {
        criminalGang.setGangArchived(!archived);
      }
      criminalGangService.updateCriminalGang(criminalGang);
    }

    return "redirect:/criminalGang/{id}" ;
  }

}




