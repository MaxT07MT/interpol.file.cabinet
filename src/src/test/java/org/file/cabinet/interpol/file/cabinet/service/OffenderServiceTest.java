package org.file.cabinet.interpol.file.cabinet.service;

import java.util.Date;
import java.util.Optional;
import org.file.cabinet.interpol.file.cabinet.model.ColorOfEyes;
import org.file.cabinet.interpol.file.cabinet.model.ColorOfHair;
import org.file.cabinet.interpol.file.cabinet.model.CriminalProfession;
import org.file.cabinet.interpol.file.cabinet.model.Offender;
import org.file.cabinet.interpol.file.cabinet.repository.OffenderRepository;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import static org.file.cabinet.interpol.file.cabinet.model.OffenderStatus.DON;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

@SpringBootTest
@Transactional
public class OffenderServiceTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private OffenderService offenderService;
  @Autowired
  private CrimeService crimeService;
  @Mock
  private OffenderRepository offenderRepository;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  //the test will fail because there are many more subjects in the database than in the test. But the test will show their real number
  @Test
  public void testGetAll() {
    Offender offender1 = new Offender();
    offender1.setOffId(1L);
    Offender offender2 = new Offender();
    offender2.setOffId(2L);
    List<Offender> mockOffenders = new ArrayList<>();
    mockOffenders.add(offender1);
    mockOffenders.add(offender2);
    when(offenderRepository.findAll()).thenReturn(mockOffenders);
    List<Offender> result = offenderService.getAll();
    assertNotNull(result);
    assertEquals(result.size(), 2);

  }

  @Test
  public void testGetByArchivedFalseOrArchivedIsNull() {
    Offender offender1 = new Offender();
    offender1.setOffId(1L);
    Offender offender2 = new Offender();
    offender2.setOffId(2L);
    List<Offender> mockOffenders = new ArrayList<>();
    mockOffenders.add(offender1);
    mockOffenders.add(offender2);
    when(offenderRepository.findByArchivedFalseOrArchivedIsNull()).thenReturn(mockOffenders);
    List<Offender> result = offenderService.getByArchivedFalseOrArchivedIsNull();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetByArchivedTrue() {
    Offender offender1 = new Offender();
    offender1.setOffId(1L);
    Offender offender2 = new Offender();
    offender2.setOffId(2L);
    List<Offender> mockOffenders = new ArrayList<>();
    mockOffenders.add(offender1);
    mockOffenders.add(offender2);
    when(offenderRepository.findByArchivedTrue()).thenReturn(mockOffenders);
    List<Offender> result = offenderService.getByArchivedTrue();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetOffenderById() {
    Offender offender = new Offender();
    offender.setOffId(1L);
    when(offenderRepository.findById(1L)).thenReturn(Optional.of(offender));
    Offender result = offenderService.getOffenderById(1L);
    assertNotNull(result);
    assertEquals(result.getOffId(), 1L);
  }

  @Test
  public void testCreateOffenderWithAllFields() {
    Offender offender = new Offender();
    offender.setLastname("Doe");
    offender.setFirstname("John");
    offender.setNickname("JD");
    offender.setHeight(180);
    offender.setWeight(70);
    offender.setColorOfEyes(ColorOfEyes.BLACK);
    offender.setColorOfHair(ColorOfHair.BROWN);
    offender.setBirthday(new Date());
    offender.setNationality("American");
    offender.setLastLocation("New York");
    offender.setLanguages("English");
    offender.setMainCriminalProfession(CriminalProfession.THIEF);
    offender.setDescription("Description");
    offender.setStatus(DON);
    offender.setLeave(false);
    offender.setArchived(false);
    offender.setPhoto(new byte[]{0x01, 0x02, 0x03});

    Offender createdOffender = offenderService.createOffender(offender);

    assertNotNull(createdOffender);
    assertNotNull(createdOffender.getOffId());
  }

  @Test
  public void testUpdateOffenderWithAllFields() {
    Offender initialOffender = new Offender();
    initialOffender.setLastname("Doe");
    initialOffender.setFirstname("John");
    initialOffender.setHeight(180);
    initialOffender.setWeight(70);
    initialOffender.setBirthday(new Date());
    initialOffender.setNationality("American");
    initialOffender.setLastLocation("New York");
    initialOffender.setLanguages("English");
    initialOffender.setMainCriminalProfession(CriminalProfession.THIEF);
    initialOffender.setDescription("Description");
    initialOffender.setStatus(DON);
    initialOffender.setLeave(false);
    initialOffender.setArchived(false);
    initialOffender.setPhoto(new byte[]{0x01, 0x02, 0x03});

    Offender createdOffender = offenderService.createOffender(initialOffender);

    createdOffender.setFirstname("UpdatedFirstName");
    createdOffender.setLastname("UpdatedLastName");

    Offender updatedOffender = offenderService.updateOffender(createdOffender);

    assertNotNull(updatedOffender);
    assertEquals("UpdatedFirstName", updatedOffender.getFirstname());
    assertEquals("UpdatedLastName", updatedOffender.getLastname());
  }

  @Test
  public void testDeleteOffender() {
    Offender offender = new Offender();
    offender.setLastname("Doe");
    offender.setFirstname("John");
    offender.setNickname("JD");
    offender.setHeight(180);
    offender.setWeight(70);
    offender.setColorOfEyes(ColorOfEyes.BLACK);
    offender.setColorOfHair(ColorOfHair.BROWN);
    offender.setBirthday(new Date());
    offender.setNationality("American");
    offender.setLastLocation("New York");
    offender.setLanguages("English");
    offender.setMainCriminalProfession(CriminalProfession.THIEF);
    offender.setDescription("Description");
    offender.setStatus(DON);
    offender.setLeave(false);
    offender.setArchived(false);
    offender.setPhoto(new byte[]{0x01, 0x02, 0x03});
    Offender createdOffender = offenderService.createOffender(offender);
    long offId = createdOffender.getOffId();
    offenderService.deleteOffender(offId);
    Offender deletedOffender = offenderService.getOffenderById(offId);
    assertNull(deletedOffender);
  }

}

