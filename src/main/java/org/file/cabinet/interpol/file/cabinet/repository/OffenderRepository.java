package org.file.cabinet.interpol.file.cabinet.repository;

import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.model.OffenderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OffenderRepository extends JpaRepository<Offender, Long> {

  List<Offender> findByArchivedFalseOrArchivedIsNull();
  List<Offender> findAll();

  List<Offender> findByArchivedTrue();
  List<Offender> findByFirstnameStartsWithIgnoreCase(String firstname);
  List<Offender> findByLastnameStartsWithIgnoreCase(String lastname);
  List<Offender> findByNicknameStartsWithIgnoreCase(String nickname);
  List<Offender> findByHeightBetween(int minHeight,int maxHeight);
  List<Offender> findByWeightBetween(int minWeight, int maxWeight);
  List<Offender> findByCrimesCrimeDangerTrue();
  List<Offender> findAllByCrimesCrimeDangerFalseOrCrimesCrimeDangerIsNull();
  List<Offender> findByStatus(OffenderStatus status);

}




