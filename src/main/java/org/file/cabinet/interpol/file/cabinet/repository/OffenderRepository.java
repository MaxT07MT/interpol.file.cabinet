package org.file.cabinet.interpol.file.cabinet.repository;

import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffenderRepository extends JpaRepository<Offender, Long> {
  List<Offender> findByGangId(long gangId);
  List<Offender> findByArchivedFalseOrArchivedIsNull();
  List<Offender> findByArchivedTrue();
  List<Offender> findByOffenderDangerFalseOrOffenderDangerIsNull();
  List<Offender> findByOffenderDangerTrue();
}

