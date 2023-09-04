package org.file.cabinet.interpol.file.cabinet.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.service.CrimeService;
import org.file.cabinet.interpol.file.cabinet.service.OffenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/crime")
public class CrimeController {

  private final CrimeService crimeService;
  private final OffenderService offenderService;

  @Autowired
  public CrimeController(CrimeService crimeService, OffenderService offenderService) {
    this.crimeService = crimeService;
    this.offenderService = offenderService;
  }

  @GetMapping("/notArchived")
  public String getAllCrime(Model model) {
    List<Crime> crimes = crimeService.getByCrimeArchivedFalseOrCrimeArchivedIsNull();
    model.addAttribute("crimes", crimes);
    return "crimes/crime-search-page";
  }

  @GetMapping("/archive")
  public String getArchiveCrimes(Model model, @RequestParam(required = false) String error) {
    List<Crime> crimes = crimeService.getByCrimeArchivedTrue();
    model.addAttribute("crimes", crimes);
    return "crimes/crime-search-page";
  }

  @GetMapping()
  public String getAllCrimes(Model model) {
    List<Crime> crimes = crimeService.getAll();
    model.addAttribute("crimes", crimes);
    return "crimes/crime-list";
  }

  @GetMapping("/{id}")
  public String getCrimeById(@PathVariable long id, Model model, String error) {
    Crime crime = crimeService.getById(id);
    model.addAttribute("crime", crime);
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage",
          "Нельзя удалять преступление, который не перемещен в архив");
    }
    return "crimes/crime-details";
  }


  @GetMapping("/create")
  public String createCrimeForm(Model model) {
    model.addAttribute("crime", new Crime());
    model.addAttribute("actionUrl", "/crime/create");
    return "crimes/crime-create";
  }

  @PostMapping("/create")
  public String createCrime(@Valid @ModelAttribute("crime") Crime crime,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("validationErrors", bindingResult.getAllErrors());
      return "crimes/crime-create";
    }
    crimeService.createCrime(crime);
    return "redirect:/crime";
  }

  @PostMapping("/{id}/uploadPhotoCrime")
  public String createCrimePhoto(@PathVariable long id,
      @RequestParam("photoCrime") MultipartFile photoFile) throws IOException {
    Crime crime = crimeService.getById(id);

    if (!photoFile.isEmpty()) {
      crime.setPhotoCrime(photoFile.getBytes());
      crimeService.updateCrime(crime);
    }
    return "redirect:/crime/edit/{id}";
  }

  @GetMapping("/{id}/photoCrime")
  public ResponseEntity<byte[]> getCrimePhotoById(@PathVariable long id) {
    Crime crime = crimeService.getById(id);

    if (crime.getPhotoCrime() != null) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);

      return new ResponseEntity<>(crime.getPhotoCrime(), headers, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/edit/{id}")
  public String editCrimeForm(@PathVariable long id, Model model) {
    Crime crime = crimeService.getById(id);
    model.addAttribute("crime", crime);
    model.addAttribute("actionUrl", "/crime/edit/" + id);
    return "crimes/crime-edit";
  }

  @PostMapping("/edit/{id}")
  public String editCrime(@Valid
  @ModelAttribute Crime crime, BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("validationErrors", bindingResult.getAllErrors());
      return "crimes/crime-edit";
    }
    crimeService.updateCrime(crime);
    return "redirect:/crime/{id}";
  }

  @GetMapping("/delete/{id}")
  public String deleteCrime(@PathVariable long id, Model model) {
    Crime crime = crimeService.getById(id);
    if (crime != null && Boolean.TRUE.equals(crime.getCrimeArchived())) {
      crimeService.deleteCrime(id);
      return "redirect:/crime";
    } else {

      return "redirect:/crime/{id}?error=deleteNotArchived";
    }
  }

  @GetMapping("/{id}/add-offender")
  public String getOffenderToCrime(@PathVariable long id, Model model) {
    Crime crime = crimeService.getById(id);
    List<Offender> offenders = offenderService.getAll();
    model.addAttribute("crime", crime);
    model.addAttribute("offenders", offenders);
    return "crimes/crime-add-to-offender";
  }

  @PostMapping("/{id}/add-offender")
  public String addOffenderToCrime(@PathVariable long id, @RequestParam long offId) {
    Crime crime = crimeService.getById(id);
    Offender offender = offenderService.getOffenderById(offId);
    crime.getOffenders().add(offender);
    crimeService.updateCrime(crime);
    return "redirect:/crime/edit/{id}";
  }

  @PostMapping("/{id}/toggleArchive")
  public String crimeToggleArchive(@PathVariable long id) {
    Crime crime = crimeService.getById(id);
    if (crime != null) {
      Boolean archived = crime.getCrimeArchived();
      if (archived == null) {
        crime.setCrimeArchived(false);
      } else {
        crime.setCrimeArchived(!archived);
      }
      crimeService.updateCrime(crime);
    }

    return "redirect:/crime/" + id;
  }

  @GetMapping("/searchPage")
  public String searchCrimesPage(Model model) {
    List<Crime> crimes = crimeService.getAll();
    model.addAttribute("crimes", crimes);
    return "crimes/crime-search-page";
  }

  @PostMapping("/searchByCrimeDanger")
  public String searchCrimesByCrimeDanger(Model model) {
    List<Crime> crimes = crimeService.getByCrimeDangerTrue();
    model.addAttribute("crimes", crimes);
    return "crimes/crime-search-page";
  }

  @PostMapping("/searchByCrimeNotDanger")
  public String searchCrimesByCrimeNotDanger(Model model) {
    List<Crime> crimes = crimeService.getByCrimeDangerFalseOrCrimeDangerIsNull();
    model.addAttribute("crimes", crimes);
    return "crimes/crime-search-page";
  }


  @PostMapping("/searchByDateOfCrime")
  public String searchCrimesByDate(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
      Model model) {
    List<Crime> crimes = crimeService.getByDateOfCrimeBetween(startDate, endDate);
    model.addAttribute("crimes", crimes);
    return "crimes/crime-search-page";
  }
  @GetMapping("/searchByName")
  public String searchCrimeByStartingLetters(
      @RequestParam("startingLetters") String name,
      Model model) {
    List<Crime> crimes = crimeService.getByNameStartsWithIgnoreCase(name);
    model.addAttribute("crimes", crimes);
    return "crimes/crime-search-page";
  }
}


