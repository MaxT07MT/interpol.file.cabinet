package org.file.cabinet.interpol.file.cabinet.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CrimeRepository extends JpaRepository<Crime, Long> {
  List<Crime> findByNameStartsWithIgnoreCase(String name);
  List<Crime> findByCrimeArchivedFalseOrCrimeArchivedIsNull();

  List<Crime> findByCrimeArchivedTrue();

  List<Crime> findByCrimeSolvedFalseOrCrimeSolvedIsNull();

  List<Crime> findByCrimeSolvedTrue();

  List<Crime> findByCrimeDangerFalseOrCrimeDangerIsNull();

  List<Crime> findByCrimeDangerTrue();

  List<Crime> findByCrimeDanger(Boolean crimeDanger);

  List<Crime> findByDateOfCrimeBetween(Date startDate, Date endDate);

}



