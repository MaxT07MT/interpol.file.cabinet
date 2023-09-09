package org.file.cabinet.interpol.file.cabinet.repository;

import java.util.Date;
import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrimeRepository extends JpaRepository<Crime, Long> {
  List<Crime> findByNameStartsWithIgnoreCase(String name);
  List<Crime> findByCrimeArchivedFalseOrCrimeArchivedIsNull();

  List<Crime> findByCrimeArchivedTrue();


  List<Crime> findByCrimeDangerFalseOrCrimeDangerIsNull();

  List<Crime> findByCrimeDangerTrue();

  //List<Crime> findByCrimeDanger(Boolean crimeDanger);

  List<Crime> findByDateOfCrimeBetween(Date startDate, Date endDate);

}



