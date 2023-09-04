package org.file.cabinet.interpol.file.cabinet.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.Offender;

public interface CrimeService {
  List<Crime> getAll();
  List<Crime> getByCrimeArchivedFalseOrCrimeArchivedIsNull();
  List<Crime> getByCrimeArchivedTrue();
 // List<Crime> getByCrimeSolvedFalseOrCrimeSolvedIsNull();
 // List<Crime> getByCrimeSolvedTrue();
  List<Crime> getByCrimeDangerFalseOrCrimeDangerIsNull();
  List<Crime> getByCrimeDangerTrue();
 // List<Crime> getByCrimeDanger(Boolean crimeDanger);
  List<Crime> getByDateOfCrimeBetween(Date startDate, Date endDate);
  List<Crime> getByNameStartsWithIgnoreCase(String name);
  Crime getById(long id);
  Crime createCrime(Crime crime);
  Crime updateCrime(Crime crime);
  void deleteCrime(long id);


}
