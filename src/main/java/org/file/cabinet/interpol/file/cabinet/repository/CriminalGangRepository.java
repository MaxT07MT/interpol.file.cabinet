package org.file.cabinet.interpol.file.cabinet.repository;

import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriminalGangRepository extends JpaRepository<CriminalGang, Long> {

  List<CriminalGang> findByGangArchivedFalseOrGangArchivedIsNull();
  List<CriminalGang> findByGangArchivedTrue();
  List<CriminalGang> findByGangDangerFalseOrGangDangerIsNull();
  List<CriminalGang> findByGangDangerTrue();
}
