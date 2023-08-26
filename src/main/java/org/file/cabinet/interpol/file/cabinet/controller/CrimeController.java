package org.file.cabinet.interpol.file.cabinet.controller;

import java.io.IOException;
import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.service.CrimeService;
import org.file.cabinet.interpol.file.cabinet.service.OffenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  public String getAllCrime(Model model, @RequestParam(required = false) String error) {
    List<Crime> notArchiveCrimes = crimeService.findByCrimeArchivedFalseOrCrimeArchivedIsNull();
    model.addAttribute("notArchiveCrimes", notArchiveCrimes);
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять  преступление, пока оно не перемещено в архив");
    }
    return "crimes/crime-list";
  }
  @GetMapping("/archive")
  public String getArchiveCrimes(Model model, @RequestParam(required = false) String error){
    List<Crime> archivedCrimes = crimeService.findByCrimeArchivedTrue();
    model.addAttribute("archivedCrimes", archivedCrimes);
    return "crimes/crime-archive";
  }


  @GetMapping("/{id}")
  public String getCrimeById(@PathVariable long id, Model model) {
    Crime crime = crimeService.findById(id);
    model.addAttribute("crime", crime);
    return "crimes/crime-details";
  }


  @GetMapping("/create")
  public String createCrimeForm(Model model ) {
    model.addAttribute("crime", new Crime());
    model.addAttribute("actionUrl", "/crime/create");
    return "crimes/crime-form";
  }

  @PostMapping("/create")
  public String createCrime(@ModelAttribute Crime crime) {
    crimeService.create(crime);
    return "redirect:/crime/notArchived";
  }
  @PostMapping("/{id}/uploadPhotoCrime")
  public String createCrimePhoto(@PathVariable long id, @RequestParam("photoCrime") MultipartFile photoFile) throws IOException {
    Crime crime = crimeService.findById(id);

    if (!photoFile.isEmpty()) {
      crime.setPhotoCrime(photoFile.getBytes());
      crimeService.update(crime);
    }
    return "redirect:/crime/" + id;
  }
  @GetMapping("/{id}/photoCrime")
  public ResponseEntity<byte[]> getCrimePhotoById(@PathVariable long id) {
    Crime crime = crimeService.findById(id);

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
    Crime crime = crimeService.findById(id);
    model.addAttribute("crime", crime);
    model.addAttribute("actionUrl", "/crime/edit/" + id);
    return "crimes/crime-form";
  }
  @PostMapping("/edit/{id}")
  public String editCrime(@PathVariable long id, @ModelAttribute Crime crime) {
    crimeService.update(crime);
    return "redirect:/crime/{id}";
  }

  @GetMapping("/delete/{id}")
  public String deleteCrime(@PathVariable long id) {
    crimeService.delete(id);
    return "redirect:/crime/archived";
  }

  @GetMapping("/{id}/add-offender")
  public String getOffenderToCrime(@PathVariable long id, Model model) {
    Crime crime = crimeService.findById(id);
    List<Offender> activeOffenders = offenderService.findByArchivedFalseOrArchivedIsNull();
    model.addAttribute("crime", crime);
    model.addAttribute("activeOffenders", activeOffenders);
    return "crimes/add-offender-to-crime";
  }

  @PostMapping("/{id}/add-offender")
  public String addOffenderToCrime(@PathVariable long id, @RequestParam long offId) {
    Crime crime = crimeService.findById(id);
    Offender offender = offenderService.getOffenderById(offId);
    crime.getOffenders().add(offender);
    crimeService.update(crime);
    return "redirect:/crime/" + id;
  }
  @PostMapping("/{id}/toggleArchive")
  public String crimeToggleArchive(@PathVariable long id) {
    Crime crime = crimeService.findById(id);
    if (crime != null) {
      Boolean archived = crime.getCrimeArchived();
      if (archived == null) {
        crime.setCrimeArchived(false);
      } else {
        crime.setCrimeArchived(!archived);
      }
      crimeService.update(crime);
    }

    return "redirect:/crime/" + id;
  }
}
