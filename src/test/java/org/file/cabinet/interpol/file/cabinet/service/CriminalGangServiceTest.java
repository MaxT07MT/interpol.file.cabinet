package org.file.cabinet.interpol.file.cabinet.service;

import java.util.Optional;
import org.file.cabinet.interpol.file.cabinet.model.CriminalGang;
import org.file.cabinet.interpol.file.cabinet.repository.CriminalGangRepository;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@SpringBootTest
@Transactional
public class CriminalGangServiceTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private CriminalGangService criminalGangService;
  @Mock
  private CriminalGangRepository criminalGangRepository;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetAll() {
    CriminalGang gang1 = new CriminalGang();
    gang1.setId(1L);
    CriminalGang gang2 = new CriminalGang();
    gang2.setId(2L);
    List<CriminalGang> mockGangs = new ArrayList<>();
    mockGangs.add(gang1);
    mockGangs.add(gang2);
    when(criminalGangRepository.findAll()).thenReturn(mockGangs);
    List<CriminalGang> result = criminalGangService.getAll();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetByGangArchivedFalseOrGangArchivedIsNull() {
    CriminalGang gang1 = new CriminalGang();
    gang1.setId(1L);
    CriminalGang gang2 = new CriminalGang();
    gang2.setId(2L);
    List<CriminalGang> mockGangs = new ArrayList<>();
    mockGangs.add(gang1);
    mockGangs.add(gang2);
    when(criminalGangRepository.findByGangArchivedFalseOrGangArchivedIsNull()).thenReturn(
        mockGangs);
    List<CriminalGang> result = criminalGangService.getByGangArchivedFalseOrGangArchivedIsNull();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetByGangArchivedTrue() {
    CriminalGang gang1 = new CriminalGang();
    gang1.setId(1L);
    CriminalGang gang2 = new CriminalGang();
    gang2.setId(2L);
    List<CriminalGang> mockGangs = new ArrayList<>();
    mockGangs.add(gang1);
    mockGangs.add(gang2);
    when(criminalGangRepository.findByGangArchivedTrue()).thenReturn(mockGangs);
    List<CriminalGang> result = criminalGangService.getByGangArchivedTrue();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetById() {
    CriminalGang gang = new CriminalGang();
    gang.setId(4L);
    when(criminalGangRepository.findById(4L)).thenReturn(Optional.of(gang));
    CriminalGang result = criminalGangService.getById(4L);
    assertNotNull(result);
    assertEquals(result.getId(), 4L);
  }


  @Test
  public void testCreateCriminalGang() {
    CriminalGang gang = new CriminalGang();
    gang.setName("Test Gang");
    gang.setLocation("Test Location");
    gang.setYearOfFoundation(new Date());
    gang.setDescription("Test Description");
    gang.setGangArchived(false);
    gang.setLogo(new byte[]{0x01, 0x02, 0x03});

    CriminalGang createdGang = criminalGangService.createCriminalGang(gang);

    assertNotNull(createdGang);
    assertNotNull(createdGang.getId());
  }

  @Test
  public void testUpdateCriminalGang() {
    CriminalGang initialGang = new CriminalGang();
    initialGang.setName("Initial Gang");
    initialGang.setLocation("Initial Location");
    initialGang.setYearOfFoundation(new Date());
    initialGang.setDescription("Initial Description");
    initialGang.setGangArchived(false);
    initialGang.setLogo(new byte[]{0x01, 0x02, 0x03});

    CriminalGang createdGang = criminalGangService.createCriminalGang(initialGang);

    createdGang.setName("Updated Gang");
    createdGang.setLocation("Updated Location");

    CriminalGang updatedGang = criminalGangService.updateCriminalGang(createdGang);

    assertNotNull(updatedGang);
    assertEquals("Updated Gang", updatedGang.getName());
    assertEquals("Updated Location", updatedGang.getLocation());
  }

  @Test
  public void testDeleteCriminalGang() {
    CriminalGang gang = new CriminalGang();
    gang.setName("Test Gang");
    gang.setLocation("Test Location");
    gang.setYearOfFoundation(new Date());
    gang.setDescription("Test Description");
    gang.setGangArchived(false);
    gang.setLogo(new byte[]{0x01, 0x02, 0x03});
    CriminalGang createdGang = criminalGangService.createCriminalGang(gang);
    long gangId = createdGang.getId();
    criminalGangService.deleteCriminalGang(gangId);
    CriminalGang deletedGang = criminalGangService.getById(gangId);
    assertNull(deletedGang);
  }

  @Test
  public void testGetByNameStartsWithIgnoreCase() {
    CriminalGang gang1 = new CriminalGang();
    gang1.setId(1L);
    CriminalGang gang2 = new CriminalGang();
    gang2.setId(2L);
    List<CriminalGang> mockGangs = new ArrayList<>();
    mockGangs.add(gang1);
    mockGangs.add(gang2);
    when(criminalGangRepository.findByNameStartsWithIgnoreCase("B")).thenReturn(mockGangs);
    List<CriminalGang> result = criminalGangService.getByNameStartsWithIgnoreCase("B");
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }
}

