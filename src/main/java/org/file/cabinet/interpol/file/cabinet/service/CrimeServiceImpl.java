package org.file.cabinet.interpol.file.cabinet.service;

import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.repository.CrimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrimeServiceImpl implements CrimeService{
  private final CrimeRepository crimeRepository;
  @Autowired
  public CrimeServiceImpl(CrimeRepository crimeRepository) {
    this.crimeRepository = crimeRepository;
  }

  @Override
  public List<Crime> findAll() {
    return crimeRepository.findAll();
  }

  @Override
  public List<Crime> findByCrimeArchivedFalseOrCrimeArchivedIsNull() {
    return crimeRepository.findByCrimeArchivedFalseOrCrimeArchivedIsNull();
  }

  @Override
  public List<Crime> findByCrimeArchivedTrue() {
    return crimeRepository.findByCrimeArchivedTrue();
  }

  @Override
  public List<Crime> findByCrimeSolvedFalseOrCrimeSolvedIsNull() {
    return crimeRepository.findByCrimeSolvedFalseOrCrimeSolvedIsNull();
  }

  @Override
  public List<Crime> findByCrimeSolvedTrue() {
    return crimeRepository.findByCrimeSolvedTrue();
  }

  @Override
  public List<Crime> findByCrimeDangerFalseOrCrimeDangerIsNull() {
    return crimeRepository.findByCrimeDangerFalseOrCrimeDangerIsNull();
  }

  @Override
  public List<Crime> findByCrimeDangerTrue() {
    return crimeRepository.findByCrimeDangerTrue();
  }

  @Override
  public Crime findById(long id) {
    return crimeRepository.findById(id).orElse(null);
  }

  @Override
  public Crime create(Crime crime) {
    return crimeRepository.save(crime);
  }

  @Override
  public Crime update(Crime crime) {
    return crimeRepository.save(crime);
  }

  @Override
  public void delete(long id) {
    crimeRepository.deleteById(id);
  }
}
