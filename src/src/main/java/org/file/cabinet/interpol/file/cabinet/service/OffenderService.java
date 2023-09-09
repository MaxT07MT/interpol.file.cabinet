package org.file.cabinet.interpol.file.cabinet.service;

import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.OffenderStatus;

public interface OffenderService {

  List<Offender> getAll();

  List<Offender> getByArchivedFalseOrArchivedIsNull();

  List<Offender> getByArchivedTrue();

  Offender getOffenderById(long offId);

  Offender createOffender(Offender offender);

  Offender updateOffender(Offender offender);

  void deleteOffender(long offId);

  Crime getLatestCrimeByOffender(Offender offender);

  boolean getIsDangerByOffender(Offender offender);
  List<Offender> getByFirstnameStartsWithIgnoreCase(String firstname);
  List<Offender> getByLastnameStartsWithIgnoreCase(String lastname);
  List<Offender> getByNicknameStartsWithIgnoreCase(String nickname);
  List<Offender> getByHeightBetween(int minHeight,int maxHeight);
  List<Offender> getByWeightBetween(int minWeight, int maxWeight);
  List<Offender> getByCrimesCrimeDangerTrue();
  List<Offender> getAllByCrimesCrimeDangerFalseOrCrimesCrimeDangerIsNull();
  List<Offender> getByStatus(OffenderStatus status);

}
