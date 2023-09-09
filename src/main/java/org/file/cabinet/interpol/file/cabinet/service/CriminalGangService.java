package org.file.cabinet.interpol.file.cabinet.service;

import java.util.List;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;

public interface CriminalGangService {
 List<CriminalGang> getAll();
 List<CriminalGang> getByGangArchivedFalseOrGangArchivedIsNull();
 List<CriminalGang> getByGangArchivedTrue();

 CriminalGang getById(long id);
 CriminalGang createCriminalGang(CriminalGang criminalGang);
 CriminalGang updateCriminalGang(CriminalGang criminalGang);
 void deleteCriminalGang(long id);
 List<CriminalGang> getByNameStartsWithIgnoreCase(String name);
}


