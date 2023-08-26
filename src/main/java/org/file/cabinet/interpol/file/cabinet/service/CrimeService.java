package org.file.cabinet.interpol.file.cabinet.service;

import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;

public interface CrimeService {
  List<Crime> findAll();
  List<Crime> findByCrimeArchivedFalseOrCrimeArchivedIsNull();
  List<Crime> findByCrimeArchivedTrue();
  List<Crime> findByCrimeSolvedFalseOrCrimeSolvedIsNull();
  List<Crime> findByCrimeSolvedTrue();
  List<Crime> findByCrimeDangerFalseOrCrimeDangerIsNull();
  List<Crime> findByCrimeDangerTrue();
  Crime findById(long id);

  Crime create(Crime crime);
  Crime update(Crime crime);
  void delete(long id);

}
