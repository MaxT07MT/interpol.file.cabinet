package org.file.cabinet.interpol.file.cabinet.service;

import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import java.util.List;

public interface OffenderService {
  List<Offender> findAll();
  List<Offender> findByArchivedFalseOrArchivedIsNull();
  List<Offender> findByArchivedTrue();
  List<Offender> findByOffenderDangerFalseOrOffenderDangerIsNull();
  List<Offender> findByOffenderDangerTrue();
  Offender getOffenderById(long offId);
  Offender createOffender(Offender offender);
  Offender updateOffender(Offender offender);
  void deleteOffender(long offId);

  Crime getLatestCrimeByOffender(Offender offender);
  boolean getIsDangerByOffender(Offender offender);
}

