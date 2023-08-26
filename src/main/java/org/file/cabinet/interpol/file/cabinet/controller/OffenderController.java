package org.file.cabinet.interpol.file.cabinet.controller;

import java.io.IOException;
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
@RequestMapping("/offenders")
public class OffenderController {

  private final OffenderService offenderService;
  private final CrimeService crimeService;
  private final CriminalGangService criminalGangService;
  @Autowired
  public OffenderController(OffenderService offenderService, CrimeService crimeService, CriminalGangService criminalGangService) {
    this.offenderService = offenderService;
    this.crimeService = crimeService;
    this.criminalGangService = criminalGangService;
  }
  //Поиск всех нарушителей из базы данных
  @GetMapping
  public String getOffenders(Model model, @RequestParam(required = false) String error){
    List<Offender> offenders = offenderService.findAll();
    model.addAttribute("offenders" , offenders);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять действующего преступника");
    }
    return "offenders/offender-all-list";

  }
  //Поиск всех не отошедших от дел преступников
  @GetMapping("/notArchived")
  public String getAllOffenders(Model model, @RequestParam(required = false) String error) {
    List<Offender> activeOffenders = offenderService.findByArchivedFalseOrArchivedIsNull();
    model.addAttribute("activeOffenders", activeOffenders);
     if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять действующего преступника");
    }
    return "offenders/offender-list";

  }
  //Поиск преступников помещенных в архив
  @GetMapping("/archive")
  public String getArchiveOffenders(Model model, @RequestParam(required = false) String error){
    List<Offender> archivedOffenders = offenderService.findByArchivedTrue();
    model.addAttribute("archivedOffenders", archivedOffenders);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    return "offenders/offender-archive";
  }

  //Поиск конкретного преступника по его id, перенаправление на личную страницу
  @GetMapping("/{offId}")
  public String getOffenderById(@PathVariable long offId, Model model) {
    Offender offender = offenderService.getOffenderById(offId);
    Crime latestCrime = offenderService.getLatestCrimeByOffender(offender);
    boolean isDanger = offenderService.getIsDangerByOffender(offender);

    model.addAttribute("offender", offender);
    model.addAttribute("latestCrime", latestCrime);
    model.addAttribute("isDanger", isDanger);

    return "offenders/offender-details";
  }
  //Переход на страницу добавления/редактирования преступника
  @GetMapping("/create")
  public String createOffenderForm(Model model ) {
    model.addAttribute("offender", new Offender());
    model.addAttribute("actionUrl", "/offenders/create");
    return "offenders/offender-form";
  }
  //Создание нового преступника
  @PostMapping("/create")
  public String createOffender(@ModelAttribute Offender offender) {
    offenderService.createOffender(offender);
    return "redirect:/offenders/notArchived";
  }
  //Добавление фотографии преступника в его личный профиль
  @PostMapping("/{offId}/uploadPhoto")
  public String uploadOffenderPhoto(@PathVariable long offId, @RequestParam("photo") MultipartFile photoFile) throws IOException {
    Offender offender = offenderService.getOffenderById(offId);

    if (!photoFile.isEmpty()) {
      offender.setPhoto(photoFile.getBytes());
      offenderService.updateOffender(offender);
    }
    return "redirect:/offenders/" + offId;
  }
  //Отображение фотографии преступника в его личном профиле
  @GetMapping("/{offId}/photo")
  public ResponseEntity<byte[]> getOffenderPhotoById(@PathVariable long offId) {
    Offender offender = offenderService.getOffenderById(offId);

    if (offender.getPhoto() != null) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);

      return new ResponseEntity<>(offender.getPhoto(), headers, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  //Переход на страницу добавления/редактирования преступника
  @GetMapping("/edit/{offId}")
  public String editOffenderForm(@PathVariable long offId, Model model) {
    Offender offender = offenderService.getOffenderById(offId);
    model.addAttribute("offender", offender);
    model.addAttribute("actionUrl", "/offenders/edit/" + offId);
    return "offenders/offender-form";
  }

  // Редактирование информации о преступнике
  @PostMapping("/edit/{offId}")
  public String editOffender(@PathVariable long offId, @ModelAttribute Offender offender) {
    offenderService.updateOffender(offender);
    return "redirect:/offenders/{offId}";
  }
  //Удаление преступника . Метод накладывает ограничения по удалению живых , опасных и не находящихся
  //в архиве преступников
  @GetMapping("/delete/{offId}")
  public String deleteOffender(@PathVariable long offId) {
    Offender offender = offenderService.getOffenderById(offId);

    if (Boolean.TRUE.equals(offender.getLeave())) {
      return "redirect:/offenders?error=deleteNotAllowed";
    }

    if (offender.getArchived() != null && offender.getArchived()) {
      List<Crime> crimes =  offender.getCrimes();
      if (!crimes.isEmpty()) {
        for (Crime crime : crimes) {
          crime.getOffenders().remove(offender);
        }
      }

      offenderService.deleteOffender(offId);
      return "redirect:/offenders/archive";
    } else {
      return "redirect:/offenders?error=deleteNotArchived";
    }
  }

  @GetMapping("/{offId}/crimes")
  public String getOffenderCrimes(@PathVariable long offId, Model model) {
    Offender offender = offenderService.getOffenderById(offId);
    model.addAttribute("offender", offender);
    return "offenders/offender-details";
  }

 // @GetMapping("/{offId}/add-crime")
 // public String showAddCrimeForm(@PathVariable long offId, Model model) {
  //  Offender offender = offenderService.getOffenderById(offId);
   // List<Crime> allCrimes = crimeService.findAll();
   // model.addAttribute("offender", offender);
   // model.addAttribute("allCrimes", allCrimes);
   // return "add-crime-to-offender";
  //}
 // @PostMapping("/{offId}/add-crime")
  //public String addCrimeToOffender(@PathVariable long offId, @RequestParam long crimeId) {
    //Offender offender = offenderService.getOffenderById(offId);
    //Crime crime = crimeService.findById(crimeId);
    //offender.getCrimes().add(crime);
    //offenderService.updateOffender(offender);
    //return "redirect:/offenders/" + offId;
  //}

  //Показ списка преступных группировок для выбора
  @GetMapping("/{offId}/gangs")
  public String showGangsForOffender(@PathVariable long offId, Model model) {
    Offender offender = offenderService.getOffenderById(offId);
    List<CriminalGang> allGangs = criminalGangService.findAll();

    model.addAttribute("offender", offender);
    model.addAttribute("allGangs", allGangs);

    return "offenders/add-offender-to-gang";
  }
  //Выбор преступной группировки для преступника
  @PostMapping("/{offId}/join-gang")
  public String joinGang(
      @PathVariable long offId,
      @RequestParam long id
  ) {
    Offender offender = offenderService.getOffenderById(offId);

    if (id == 0) {
      offender.setGang(null);
    } else {
      CriminalGang gang = criminalGangService.findById(id);
      if (gang != null) {
        offender.setGang(gang);
      }
    }
    offenderService.updateOffender(offender);
    return "redirect:/offenders/" + offId;
  }
  //Добавление в архив/извлечение из архива преступника
  @PostMapping("/{offId}/toggleArchive")
  public String toggleArchive(@PathVariable long offId) {
    Offender offender = offenderService.getOffenderById(offId);
    if (offender != null) {
      Boolean archived = offender.getArchived();
      if (archived == null) {
        offender.setArchived(false);
      } else {
        offender.setArchived(!archived);
      }
      offenderService.updateOffender(offender);
    }

    return "redirect:/offenders/" + offId;
  }
}







