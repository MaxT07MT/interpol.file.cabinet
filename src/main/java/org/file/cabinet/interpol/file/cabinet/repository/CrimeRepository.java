package org.file.cabinet.interpol.file.cabinet.repository;

import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrimeRepository extends JpaRepository<Crime, Long> {
  List<Crime> findByCrimeArchivedFalseOrCrimeArchivedIsNull();
  List<Crime> findByCrimeArchivedTrue();
  List<Crime> findByCrimeSolvedFalseOrCrimeSolvedIsNull();
  List<Crime> findByCrimeSolvedTrue();
  List<Crime> findByCrimeDangerFalseOrCrimeDangerIsNull();
  List<Crime> findByCrimeDangerTrue();
}