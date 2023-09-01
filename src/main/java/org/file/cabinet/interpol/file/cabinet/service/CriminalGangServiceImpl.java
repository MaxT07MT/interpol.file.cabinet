package org.file.cabinet.interpol.file.cabinet.service;

import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.file.cabinet.interpol.file.cabinet.repository.CrimeRepository;
import org.file.cabinet.interpol.file.cabinet.repository.CriminalGangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriminalGangServiceImpl implements CriminalGangService {

  private final CriminalGangRepository criminalGangRepository;
  private final CrimeRepository crimeRepository;

  @Autowired
  public CriminalGangServiceImpl(CriminalGangRepository criminalGangRepository,
      CrimeRepository crimeRepository) {
    this.criminalGangRepository = criminalGangRepository;
    this.crimeRepository = crimeRepository;
  }

  @Override
  public List<CriminalGang> getAll() {
    return criminalGangRepository.findAll();
  }

  @Override
  public List<CriminalGang> getByGangArchivedFalseOrGangArchivedIsNull() {
    return criminalGangRepository.findByGangArchivedFalseOrGangArchivedIsNull();
  }

  @Override
  public List<CriminalGang> getByGangArchivedTrue() {
    return criminalGangRepository.findByGangArchivedTrue();
  }


  @Override
  public CriminalGang getById(long id) {
    return criminalGangRepository.findById(id).orElse(null);
  }

  @Override
  public CriminalGang createCriminalGang(CriminalGang criminalGang) {
    return criminalGangRepository.save(criminalGang);
  }

  @Override
  public CriminalGang updateCriminalGang(CriminalGang criminalGang) {
    return criminalGangRepository.save(criminalGang);
  }

  @Override
  public void deleteCriminalGang(long id) {
    criminalGangRepository.deleteById(id);
  }

  @Override
  public List<CriminalGang> getByNameStartsWithIgnoreCase(String name) {
    return criminalGangRepository.findByNameStartsWithIgnoreCase(name);
  }

}

