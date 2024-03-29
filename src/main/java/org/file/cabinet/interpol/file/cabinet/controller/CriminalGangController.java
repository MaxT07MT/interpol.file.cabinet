package org.file.cabinet.interpol.file.cabinet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/criminalGang")
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
    List<CriminalGang> criminalGangs = criminalGangService.getAll();
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/criminal-gang-list";
  }

  @GetMapping("/notArchived")
  public String getAllGangs(Model model, @RequestParam(required = false) String error) {
    List<CriminalGang> criminalGangs = criminalGangService.getByGangArchivedFalseOrGangArchivedIsNull();
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/criminal-gangs-search-page";

  }


  @GetMapping("/archive")
  public String getArchivedGangs(Model model, @RequestParam(required = false) String error) {
    List<CriminalGang> criminalGangs = criminalGangService.getByGangArchivedTrue();
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/criminal-gangs-search-page";
  }


  @GetMapping("/{id}")
  public String getCriminalGangById(@PathVariable long id, Model model, String error) {
    CriminalGang criminalGang = criminalGangService.getById(id);
    Set<Offender> offenders = criminalGang.getOffenders();
    Set<Crime> crimes = new HashSet<>();
    for (Offender offender : offenders) {
      crimes.addAll(offender.getCrimes());
    }
    model.addAttribute("criminalGang", criminalGang);
    model.addAttribute("crimes", crimes);
    if ("deleteGangNotArchived".equals(error)) {
      model.addAttribute("delErrorMessage", "Нельзя удалять gang, который не перемещен в архив");
    }

    return "gangs/criminal-gang-details";
  }


  @GetMapping("/create")
  public String createCriminalGangForm(Model model) {
    model.addAttribute("criminalGang", new CriminalGang());
    model.addAttribute("actionUrl", "/criminalGang/create");
    return "gangs/criminal-gang-create";
  }

  @PostMapping("/create")
  public String createCriminalGang(@Valid @ModelAttribute("criminalGang") CriminalGang criminalGang,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("validationErrors", bindingResult.getAllErrors());
      return "gangs/criminal-gang-create";
    }
    criminalGangService.createCriminalGang(criminalGang);
    return "redirect:/criminalGang";
  }

  @PostMapping("/{id}/uploadLogo")
  public String uploadCriminalGangLogo(@PathVariable long id,
      @RequestParam("logo") MultipartFile logoFile) throws IOException {
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
  public String editCriminalGang(@Valid
  @ModelAttribute CriminalGang criminalGang, BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("validationErrors", bindingResult.getAllErrors());
      return "gangs/criminal-gang-edit";
    }
    criminalGangService.updateCriminalGang(criminalGang);
    return "redirect:/criminalGang/{id}";
  }


  @GetMapping("/delete/{id}")
  public String deleteCriminalGang(@PathVariable long id) {
    CriminalGang gang = criminalGangService.getById(id);
    if (gang == null) {
      return "redirect:/criminalGang";
    }
    Boolean gangArchived = gang.getGangArchived();
    if (Boolean.TRUE.equals(gangArchived)) {
      Set<Offender> offenders = gang.getOffenders();

      if (!offenders.isEmpty()) {
        for (Offender offender : offenders) {
          offender.setGang(null);
          offenderService.updateOffender(offender);
        }
      }
      criminalGangService.deleteCriminalGang(id);
      return "redirect:/criminalGang";
    } else {
      return "redirect:/criminalGang/{id}?error=deleteGangNotArchived";
    }
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
  public String searchCriminalGangsPage(Model model) {
    List<CriminalGang> criminalGangs = criminalGangService.getAll();
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/criminal-gangs-search-page";
  }

  @GetMapping("/searchByName")
  public String searchGangsByStartingLetters(
      @RequestParam("startingLetters") String name,
      Model model) {
    List<CriminalGang> criminalGangs = criminalGangService.getByNameStartsWithIgnoreCase(name);
    model.addAttribute("criminalGangs", criminalGangs);
    return "gangs/criminal-gangs-search-page";
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

    return "redirect:/criminalGang/{id}";
  }

}




