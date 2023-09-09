package org.file.cabinet.interpol.file.cabinet.service;

import java.util.Optional;
import org.file.cabinet.interpol.file.cabinet.model.Crime;
import org.file.cabinet.interpol.file.cabinet.repository.CrimeRepository;
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
public class CrimeServiceTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private CrimeService crimeService;
  @Mock
  private CrimeRepository crimeRepository;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetAll() {
    Crime crime1 = new Crime();
    crime1.setId(1L);
    Crime crime2 = new Crime();
    crime2.setId(2L);
    List<Crime> mockCrimes = new ArrayList<>();
    mockCrimes.add(crime1);
    mockCrimes.add(crime2);
    when(crimeRepository.findAll()).thenReturn(mockCrimes);
    List<Crime> result = crimeService.getAll();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetByCrimeArchivedFalseOrCrimeArchivedIsNull() {
    Crime crime1 = new Crime();
    crime1.setId(1L);
    Crime crime2 = new Crime();
    crime2.setId(2L);
    List<Crime> mockCrimes = new ArrayList<>();
    mockCrimes.add(crime1);
    mockCrimes.add(crime2);
    when(crimeRepository.findByCrimeArchivedFalseOrCrimeArchivedIsNull()).thenReturn(mockCrimes);
    List<Crime> result = crimeService.getByCrimeArchivedFalseOrCrimeArchivedIsNull();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetByCrimeArchivedTrue() {
    Crime crime1 = new Crime();
    crime1.setId(1L);
    Crime crime2 = new Crime();
    crime2.setId(2L);
    List<Crime> mockCrimes = new ArrayList<>();
    mockCrimes.add(crime1);
    mockCrimes.add(crime2);
    when(crimeRepository.findByCrimeArchivedTrue()).thenReturn(mockCrimes);
    List<Crime> result = crimeService.getByCrimeArchivedTrue();
    assertNotNull(result);
    assertEquals(result.size(), 2);
  }

  @Test
  public void testGetById() {
    Crime crime = new Crime();
    crime.setId(1L);
    when(crimeRepository.findById(1L)).thenReturn(Optional.of(crime));
    Crime result = crimeService.getById(1L);
    assertNotNull(result);
    assertEquals(result.getId(), 1L);
  }

  @Test
  public void testCreateCrime() {
    Crime crime = new Crime();
    crime.setName("Test Crime");
    crime.setPlace("Test Place");
    crime.setDateOfCrime(new Date());
    crime.setDescription("Test Description");
    crime.setCrimeSolved(false);
    crime.setCrimeDanger(true);
    crime.setPhotoCrime(new byte[]{0x01, 0x02, 0x03});

    Crime createdCrime = crimeService.createCrime(crime);

    assertNotNull(createdCrime);
    assertNotNull(createdCrime.getId());
  }

  @Test
  public void testUpdateCrime() {
    Crime initialCrime = new Crime();
    initialCrime.setName("Initial Crime");
    initialCrime.setPlace("Initial Place");
    initialCrime.setDateOfCrime(new Date());
    initialCrime.setDescription("Initial Description");
    initialCrime.setCrimeSolved(false);
    initialCrime.setCrimeDanger(true);
    initialCrime.setPhotoCrime(new byte[]{0x01, 0x02, 0x03});

    Crime createdCrime = crimeService.createCrime(initialCrime);

    createdCrime.setName("Updated Crime");
    createdCrime.setPlace("Updated Place");

    Crime updatedCrime = crimeService.updateCrime(createdCrime);

    assertNotNull(updatedCrime);
    assertEquals("Updated Crime", updatedCrime.getName());
    assertEquals("Updated Place", updatedCrime.getPlace());
  }

  @Test
  public void testDeleteCrime() {
    Crime crime = new Crime();
    crime.setName("Test Crime");
    crime.setPlace("Test Place");
    crime.setDateOfCrime(new Date());
    crime.setDescription("Test Description");
    crime.setCrimeSolved(false);
    crime.setCrimeDanger(true);
    crime.setPhotoCrime(new byte[]{0x01, 0x02, 0x03});
    Crime createdCrime = crimeService.createCrime(crime);
    long crimeId = createdCrime.getId();
    crimeService.deleteCrime(crimeId);
    Crime deletedCrime = crimeService.getById(crimeId);
    assertNull(deletedCrime);
  }
}
