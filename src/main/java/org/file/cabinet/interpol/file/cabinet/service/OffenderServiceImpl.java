package org.file.cabinet.interpol.file.cabinet.service;


import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.model.OffenderStatus;
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
  public List<Offender> getAll() {
    return offenderRepository.findAll();
  }

  @Override
  public List<Offender> getByArchivedFalseOrArchivedIsNull() {
    return offenderRepository.findByArchivedFalseOrArchivedIsNull();
  }

  @Override
  public List<Offender> getByArchivedTrue() {
    return offenderRepository.findByArchivedTrue();
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
    Set<Crime> crimes = (Set<Crime>) offender.getCrimes();
    if (!crimes.isEmpty()) {
      return crimes.stream()
          .max(Comparator.comparing(Crime::getDateOfCrime))
          .orElse(null);
    }
    return null;
  }

  @Override
  public boolean getIsDangerByOffender(Offender offender) {
    Set<Crime> crimes = offender.getCrimes();

    for (Crime crime : crimes) {
      if (Boolean.TRUE.equals(crime.getCrimeDanger())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public List<Offender> getByFirstnameStartsWithIgnoreCase(String firstname) {
    return offenderRepository.findByFirstnameStartsWithIgnoreCase(firstname);
  }

  @Override
  public List<Offender> getByLastnameStartsWithIgnoreCase(String lastname) {
    return offenderRepository.findByLastnameStartsWithIgnoreCase(lastname);
  }

  @Override
  public List<Offender> getByNicknameStartsWithIgnoreCase(String nickname) {
    return offenderRepository.findByNicknameStartsWithIgnoreCase(nickname);
  }

  @Override
  public List<Offender> getByHeightBetween(int minHeight, int maxHeight) {
    return offenderRepository.findByHeightBetween(minHeight, maxHeight);
  }

  @Override
  public List<Offender> getByWeightBetween(int minWeight, int maxHeight) {
    return offenderRepository.findByWeightBetween(minWeight, maxHeight);
  }

  @Override
  public List<Offender> getByCrimesCrimeDangerTrue() {
    return offenderRepository.findByCrimesCrimeDangerTrue();
  }

  @Override
  public List<Offender> getAllByCrimesCrimeDangerFalseOrCrimesCrimeDangerIsNull() {
    return offenderRepository.findAllByCrimesCrimeDangerFalseOrCrimesCrimeDangerIsNull();
  }

  @Override
  public List<Offender> getByStatus(OffenderStatus status) {
    return offenderRepository.findByStatus(status);
  }

  }
