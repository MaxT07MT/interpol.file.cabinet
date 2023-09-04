package org.file.cabinet.interpol.file.cabinet.service;


import java.util.Date;
import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.repository.CrimeRepository;
import org.file.cabinet.interpol.file.cabinet.repository.OffenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrimeServiceImpl implements CrimeService{
  private final CrimeRepository crimeRepository;
  private final OffenderRepository offenderRepository;
  @Autowired
  public CrimeServiceImpl(CrimeRepository crimeRepository, OffenderRepository offenderRepository) {
    this.crimeRepository = crimeRepository;
    this.offenderRepository = offenderRepository;
  }

  @Override
  public List<Crime> getAll() {
    return crimeRepository.findAll();
  }

  @Override
  public List<Crime> getByCrimeArchivedFalseOrCrimeArchivedIsNull() {
    return crimeRepository.findByCrimeArchivedFalseOrCrimeArchivedIsNull();
  }

  @Override
  public List<Crime> getByCrimeArchivedTrue() {
    return crimeRepository.findByCrimeArchivedTrue();
  }

  //@Override
  //public List<Crime> getByCrimeSolvedFalseOrCrimeSolvedIsNull() {
    //return crimeRepository.findByCrimeSolvedFalseOrCrimeSolvedIsNull();
  //}

  //@Override
  //public List<Crime> getByCrimeSolvedTrue() {
    //return crimeRepository.findByCrimeSolvedTrue();
  //}

  @Override
  public List<Crime> getByCrimeDangerFalseOrCrimeDangerIsNull() {
    return crimeRepository.findByCrimeDangerFalseOrCrimeDangerIsNull();
  }

  @Override
  public List<Crime> getByCrimeDangerTrue() {
    return crimeRepository.findByCrimeDangerTrue();
  }


  @Override
  public Crime getById(long id) {
    return crimeRepository.findById(id).orElse(null);
  }

  @Override
  public Crime createCrime(Crime crime) {
    return crimeRepository.save(crime);
  }

  @Override
  public Crime updateCrime(Crime crime) {
    return crimeRepository.save(crime);
  }

  @Override
  public void deleteCrime(long id) {
    crimeRepository.deleteById(id);
  }

  //@Override
  //public List<Crime> getByCrimeDanger(Boolean crimeDanger) {
   // return crimeRepository.findByCrimeDanger(crimeDanger);
  //}

  @Override
  public List<Crime> getByDateOfCrimeBetween(Date startDate, Date endDate) {
    return crimeRepository.findByDateOfCrimeBetween(startDate, endDate);
  }

  @Override
  public List<Crime> getByNameStartsWithIgnoreCase(String name) {
    return crimeRepository.findByNameStartsWithIgnoreCase(name);
  }
}
