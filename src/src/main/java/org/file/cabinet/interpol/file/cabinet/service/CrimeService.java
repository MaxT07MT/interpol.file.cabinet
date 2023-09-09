package org.file.cabinet.interpol.file.cabinet.service;

import java.util.Date;
import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;


public interface CrimeService {
  List<Crime> getAll();
  List<Crime> getByCrimeArchivedFalseOrCrimeArchivedIsNull();
  List<Crime> getByCrimeArchivedTrue();

  List<Crime> getByCrimeDangerFalseOrCrimeDangerIsNull();
  List<Crime> getByCrimeDangerTrue();

  List<Crime> getByDateOfCrimeBetween(Date startDate, Date endDate);
  List<Crime> getByNameStartsWithIgnoreCase(String name);
  Crime getById(long id);
  Crime createCrime(Crime crime);
  Crime updateCrime(Crime crime);
  void deleteCrime(long id);


}
