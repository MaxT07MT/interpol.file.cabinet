package org.file.cabinet.interpol.file.cabinet.service;

import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.file.cabinet.interpol.file.cabinet.repository.CriminalGangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriminalGangServiceImpl implements CriminalGangService{
  private final CriminalGangRepository criminalGangRepository;
  @Autowired
  public CriminalGangServiceImpl(CriminalGangRepository criminalGangRepository) {
    this.criminalGangRepository = criminalGangRepository;
  }

  @Override
  public List<CriminalGang> findAll() {
    return criminalGangRepository.findAll();
  }

  @Override
  public List<CriminalGang> findByGangArchivedFalseOrGangArchivedIsNull() {
    return criminalGangRepository.findByGangArchivedFalseOrGangArchivedIsNull();
  }

  @Override
  public List<CriminalGang> findByGangArchivedTrue() {
    return criminalGangRepository.findByGangArchivedTrue();
  }


  @Override
  public List<CriminalGang> findByGangDangerFalseOrGangDangerIsNull() {
    return criminalGangRepository.findByGangDangerFalseOrGangDangerIsNull();
  }

  @Override
  public List<CriminalGang> findByGangDangerTrue() {
    return criminalGangRepository.findByGangDangerTrue();
  }

  @Override
  public CriminalGang findById(long id) {
    return criminalGangRepository.findById(id).orElse(null);
  }

  @Override
  public CriminalGang create(CriminalGang criminalGang) {
    return criminalGangRepository.save(criminalGang);
  }

  @Override
  public CriminalGang update(CriminalGang criminalGang) {
    return criminalGangRepository.save(criminalGang);
  }

  @Override
  public void deleteCriminalGang(long id) {
   criminalGangRepository.deleteById(id);
  }


}
