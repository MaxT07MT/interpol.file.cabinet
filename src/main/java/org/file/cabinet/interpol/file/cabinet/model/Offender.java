package org.file.cabinet.interpol.file.cabinet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "offender")
@Data

public class Offender {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long offId;
  @Column(name = "lastname")
  private String lastname;
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "nickname")
  private String nickname;
  @Column(name = "height")
  private int height;
  @Column(name = "weight")
  private int weight;
  @Column(name = "color_of_eyes")
  private String colorOfEyes;
  @Column(name = "color_of_hair")
  private String colorOfHair;
  @Column(name = "birthday")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthday;
  @Column(name = "nationality")
  private String nationality;
  @Column(name = "last_location")
  private String lastLocation;
  @Column(name = "last_criminal")
  private String lastCriminal;
  @Column(name = "languages")
  private String languages;
  @Column(name = "main_criminal_profession")
  private String mainCriminalProfession;
  @Column(name = "description")
  private String description;
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private OffenderStatus status;
  @Column(name = "leave_status")
  private Boolean leave;
  @Column(name = "archived")
  private Boolean archived;
  @Column(name = "offender_danger")
  private Boolean offenderDanger;
  @Column(name = "photo", columnDefinition = "LONGBLOB")
  @Lob
  private byte[] photo;

  @ManyToOne
  @JoinColumn(name = "gang_id")
  private CriminalGang gang;

  @ManyToMany(mappedBy = "offenders")
  private List<Crime> crimes = new ArrayList<>();

  @JoinTable(
      name = "crime_offender",
      joinColumns = @JoinColumn(name = "offender_id"),
      inverseJoinColumns = @JoinColumn(name = "crime_id")
  )
  @Override
  public int hashCode() {
    return Objects.hash(offId);
  }

}
