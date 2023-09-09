package org.file.cabinet.interpol.file.cabinet.repository;

import java.util.List;
import java.util.Set;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CriminalGangRepository extends JpaRepository<CriminalGang, Long> {

  List<CriminalGang> findByGangArchivedFalseOrGangArchivedIsNull();
  List<CriminalGang> findByGangArchivedTrue();
  List<CriminalGang> findByNameStartsWithIgnoreCase(String name);


}
