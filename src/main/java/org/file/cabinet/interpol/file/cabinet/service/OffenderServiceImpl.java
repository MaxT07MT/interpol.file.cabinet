package org.file.cabinet.interpol.file.cabinet.service;

import java.util.Comparator;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.repository.OffenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffenderServiceImpl implements OffenderService {

  private final OffenderRepository offenderRepository;

  @Autowired
  public OffenderServiceImpl(OffenderRepository offenderRepository) {
    this.offenderRepository = offenderRepository;
  }


  @Override
  public List<Offender> findAll() {
    return offenderRepository.findAll();
  }

  @Override
  public List<Offender> findByArchivedFalseOrArchivedIsNull() {
    return offenderRepository.findByArchivedFalseOrArchivedIsNull();
  }

  @Override
  public List<Offender> findByArchivedTrue() {
    return offenderRepository.findByArchivedTrue();
  }

  @Override
  public List<Offender> findByOffenderDangerFalseOrOffenderDangerIsNull() {
    return offenderRepository.findByOffenderDangerFalseOrOffenderDangerIsNull();
  }

  @Override
  public List<Offender> findByOffenderDangerTrue() {
    return offenderRepository.findByOffenderDangerTrue();
  }

  @Override
  public Offender getOffenderById(long offId) {
    return offenderRepository.findById(offId).orElse(null);
  }


  @Override
  public Offender createOffender(Offender offender) {
    return offenderRepository.save(offender);
  }

  @Override
  public Offender updateOffender(Offender offender) {
    return offenderRepository.save(offender);
  }

  @Override
  public void deleteOffender(long offId) {
    offenderRepository.deleteById(offId);
  }

  @Override
  public Crime getLatestCrimeByOffender(Offender offender) {
    List<Crime> crimes = (List<Crime>) offender.getCrimes();
    if (!crimes.isEmpty()) {
      return crimes.stream()
          .max(Comparator.comparing(Crime::getDateOfCrime))
          .orElse(null);
    }
    return null;
  }

  @Override
  public boolean getIsDangerByOffender(Offender offender) {
    List<Crime> crimes = offender.getCrimes();

    for (Crime crime : crimes) {
      if (Boolean.TRUE.equals(crime.getCrimeDanger())) {
        return true;
      }
    }

    return false;
  }



}
