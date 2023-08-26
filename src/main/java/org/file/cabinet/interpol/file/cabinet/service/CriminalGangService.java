package org.file.cabinet.interpol.file.cabinet.service;

import java.util.List;
import java.util.Optional;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;

public interface CriminalGangService {
 List<CriminalGang> findAll();
 List<CriminalGang> findByGangArchivedFalseOrGangArchivedIsNull();
 List<CriminalGang> findByGangArchivedTrue();
 List<CriminalGang> findByGangDangerFalseOrGangDangerIsNull();
 List<CriminalGang> findByGangDangerTrue();
 CriminalGang findById(long id);
 CriminalGang create(CriminalGang criminalGang);
 CriminalGang update(CriminalGang criminalGang);
 void deleteCriminalGang(long id);
}
