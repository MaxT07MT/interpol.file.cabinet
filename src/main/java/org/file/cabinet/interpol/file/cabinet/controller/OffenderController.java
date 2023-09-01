package org.file.cabinet.interpol.file.cabinet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import javax.validation.Valid;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.model.OffenderStatus;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequestMapping("/offender")
public class OffenderController {

  private final OffenderService offenderService;
  private final CrimeService crimeService;
  private final CriminalGangService criminalGangService;

  @Autowired
  public OffenderController(OffenderService offenderService, CrimeService crimeService,
      CriminalGangService criminalGangService) {
    this.offenderService = offenderService;
    this.crimeService = crimeService;
    this.criminalGangService = criminalGangService;
  }

  //Поиск всех нарушителей из базы данных
  @GetMapping
  public String getOffenders(Model model, @RequestParam(required = false) String error) {
    List<Offender> offenders = offenderService.getAll();
    model.addAttribute("offenders", offenders);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage",
          "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять действующего преступника");
    }
    return "offenders/offender-list";

  }

  //Поиск всех не отошедших от дел преступников
  @GetMapping("/notArchived")
  public String getAllOffenders(Model model, @RequestParam(required = false) String error) {
    List<Offender> offenders = offenderService.getByArchivedFalseOrArchivedIsNull();
    model.addAttribute("offenders", offenders);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage",
          "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять действующего преступника");
    }
    return "offenders/search-offenders-page";

  }

  //Поиск преступников помещенных в архив
  @GetMapping("/archive")
  public String getArchiveOffenders(Model model, @RequestParam(required = false) String error) {
    List<Offender> offenders = offenderService.getByArchivedTrue();
    model.addAttribute("offenders", offenders);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage",
          "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    return "offenders/search-offenders-page";
  }

  //Поиск конкретного преступника по его id, перенаправление на личную страницу
  @GetMapping("/{offId}")
  public String getOffenderById(@PathVariable long offId, Model model , String error) {
    Offender offender = offenderService.getOffenderById(offId);
    Crime latestCrime = offenderService.getLatestCrimeByOffender(offender);
    boolean isDanger = offenderService.getIsDangerByOffender(offender);

    model.addAttribute("offender", offender);
    model.addAttribute("latestCrime", latestCrime);
    model.addAttribute("isDanger", isDanger);
    if ("deleteNotAllowed".equals(error)) {
      model.addAttribute("deleteErrorMessage",
          "Из базы невозможно удалить преступника до его смерти. Его нужно держать в поле зрения пожизнено");
    }
    if ("deleteNotArchived".equals(error)) {
      model.addAttribute("deleteErrorMessage", "Нельзя удалять преступника, который не перемещен в архив");
    }
    return "offenders/offender-details";
  }

  //Переход на страницу поисков
  @GetMapping("/searchPage")
  public String searchOffenderPage(Model model) {
    List<Offender> offenders = offenderService.getAll();
    model.addAttribute("offenders", offenders);
    return "offenders/search-offenders-page";
  }

  @PostMapping("/searchByFirstName")
  public String searchOffendersByFirstname(@RequestParam(required = false) String firstname,
      Model model) {
    List<Offender> offenders = offenderService.getByFirstnameStartsWithIgnoreCase(firstname);
    model.addAttribute("offenders", offenders);
    return "offenders/search-offenders-page";
  }

  @PostMapping("/searchByLastName")
  public String searchOffendersByLastname(@RequestParam(required = false) String lastname,
      Model model) {
    List<Offender> offenders = offenderService.getByLastnameStartsWithIgnoreCase(lastname);
    model.addAttribute("offenders", offenders);
    return "offenders/search-offenders-page";
  }

  @PostMapping("/searchByNickname")
  public String searchOffendersByNickname(@RequestParam(required = false) String nickname,
      Model model) {
    List<Offender> offenders = offenderService.getByNicknameStartsWithIgnoreCase(nickname);
    model.addAttribute("offenders", offenders);
    return "offenders/search-offenders-page";
  }

  @PostMapping("/searchByHeight")
  public String searchOffendersByHeight(
      @RequestParam(required = false) Integer minHeight,
      @RequestParam(required = false) Integer maxHeight,
      Model model) {

    List<Offender> offenders;

    if (minHeight != null && maxHeight != null) {
      offenders = offenderService.getByHeightBetween(minHeight, maxHeight);
    } else if (minHeight != null) {
      offenders = offenderService.getByHeightBetween(minHeight, Integer.MAX_VALUE);
    } else if (maxHeight != null) {
      offenders = offenderService.getByHeightBetween(0, maxHeight);
    } else {
      offenders = new ArrayList<>();
    }

    model.addAttribute("offenders", offenders);
    return "offenders/search-offenders-page";
  }


  @PostMapping("/searchByWeight")
  public String searchOffendersByWeight(
      @RequestParam(required = false) Integer minWeight,
      @RequestParam(required = false) Integer maxWeight,
      Model model) {

    List<Offender> offenders;

    if (minWeight != null && maxWeight != null) {
      offenders = offenderService.getByWeightBetween(minWeight, maxWeight);
    } else if (minWeight != null) {
      offenders = offenderService.getByWeightBetween(minWeight, Integer.MAX_VALUE);
    } else if (maxWeight != null) {
      offenders = offenderService.getByWeightBetween(0, maxWeight);
    } else {
      offenders = new ArrayList<>();
    }

    model.addAttribute("offenders", offenders);
    return "offenders/search-offenders-page";
  }


  @GetMapping("/dangerous")
  public String getDangerousOffenders(Model model) {
    List<Offender> offenders = offenderService.getByCrimesCrimeDangerTrue();
    model.addAttribute("offenders", offenders);
    model.addAttribute("isDanger", true);
    return "offenders/search-offenders-page";
  }

  @GetMapping("/notDangerous")
  public String getNonDangerousOffenders(Model model) {
    List<Offender> offenders = offenderService.getAllByCrimesCrimeDangerFalseOrCrimesCrimeDangerIsNull();
    model.addAttribute("offenders", offenders);
    model.addAttribute("isDanger", false);
    return "offenders/search-offenders-page";
  }

  @PostMapping("/searchByStatus")
  public String searchOffendersByStatus(@RequestParam String status, Model model) {
    List<Offender> offenders = offenderService.getByStatus(OffenderStatus.valueOf(status));
    model.addAttribute("offenders", offenders);
    return "offenders/search-offenders-page";
  }


  //Переход на страницу добавления/редактирования преступника
  @GetMapping("/create")
  public String createOffenderForm(Model model) {
    model.addAttribute("offender", new Offender());
    model.addAttribute("actionUrl", "/offender/create");
    return "offenders/offender-create";
  }




  //Создание нового преступника
  @PostMapping("/create")
  public String createOffender(@Valid @ModelAttribute("offender") Offender offender, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("validationErrors", bindingResult.getAllErrors());
      return "offenders/offender-create";
    }
    offenderService.createOffender(offender);
    model.addAttribute("actionUrl", "/offender/create");
    return "redirect:/offender";
  }


  //Добавление фотографии преступника в его личный профиль
  @PostMapping("/{offId}/uploadPhoto")
  public String uploadOffenderPhoto(@PathVariable long offId,
      @RequestParam("photo") MultipartFile photoFile) throws IOException {
    Offender offender = offenderService.getOffenderById(offId);

    if (!photoFile.isEmpty()) {
      offender.setPhoto(photoFile.getBytes());
      offenderService.updateOffender(offender);
    }
    return "redirect:/offender/edit/{offId}";
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
    model.addAttribute("actionUrl", "/offender/edit/" + offId);
    return "offenders/offender-edit";
  }

  // Редактирование информации о преступнике
  @PostMapping("/edit/{offId}")
  public String editOffender(@Valid
      @ModelAttribute Offender offender,BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("validationErrors", bindingResult.getAllErrors());
      return "offenders/offender-edit";
 }
    offenderService.updateOffender(offender);
    return "redirect:/offender/{offId}";
  }

  //Удаление преступника . Метод накладывает ограничения по удалению живых , опасных и не находящихся
  //в архиве преступников
  @GetMapping("/delete/{offId}")
  public String deleteOffender(@PathVariable long offId) {
    Offender offender = offenderService.getOffenderById(offId);

    if (Boolean.TRUE.equals(offender.getLeave())) {
      return "redirect:/offender/{offId}?error=deleteNotAllowed";
    }

    if (offender.getArchived() != null && offender.getArchived()) {
      Set<Crime> crimes =  offender.getCrimes();
      if (!crimes.isEmpty()) {
        for (Crime crime : crimes) {
          crime.getOffenders().remove(offender);
        }
      }

      offenderService.deleteOffender(offId);
      return "redirect:/offender";
    } else {
      return "redirect:/offender/{offId}?error=deleteNotArchived";
    }
  }


  @GetMapping("/{offId}/crimes")
  public String getOffenderCrimes(@PathVariable long offId, Model model) {
    Offender offender = offenderService.getOffenderById(offId);
    model.addAttribute("offender", offender);
    return "offenders/offender-details";
  }


  //Показ списка преступных группировок для выбора
  @GetMapping("/{offId}/gangs")
  public String showGangsForOffender(@PathVariable long offId, Model model) {
    Offender offender = offenderService.getOffenderById(offId);
    List<CriminalGang> criminalGangs = criminalGangService.getAll();

    model.addAttribute("offender", offender);
    model.addAttribute("criminalGangs", criminalGangs);

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
      CriminalGang gang = criminalGangService.getById(id);
      if (gang != null) {
        offender.setGang(gang);
      }
    }
    offenderService.updateOffender(offender);
    return "redirect:/offender/edit/{offId}";
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

    return "redirect:/offender/" + offId;
  }
}







