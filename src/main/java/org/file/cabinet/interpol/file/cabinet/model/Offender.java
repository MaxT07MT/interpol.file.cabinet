package org.file.cabinet.interpol.file.cabinet.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "offender")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offender {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long offId;
  @NotBlank(message = "Lastname cannot be empty")
  @Column(name = "lastname")
  private String lastname;
  @NotBlank(message = "Firstname cannot be empty")
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "nickname")
  private String nickname;

  @Min(value = 120, message = "Height should be at least 120")
  @Max(value = 220, message = "Height should not exceed 220")
  @Column(name = "height")
  private int height;

  @Min(value = 40, message = "Height should be at least 40")
  @Max(value = 180, message = "Height should not exceed 180")
  @Column(name = "weight")
  private int weight;
  @Column(name = "color_of_eyes")
  @Enumerated(EnumType.STRING)
  private ColorOfEyes colorOfEyes;
  @Column(name = "color_of_hair")
  @Enumerated(EnumType.STRING)
  private ColorOfHair colorOfHair;
  //@Pattern(regexp = "^(0[1-9]|1[0-9]|2[0-9]|3[0-1])\\.(0[1-9]|1[0-2])\\.(18[7-9]\\d|19[0-9]\\d|2000)$", message = "Invalid date. Date should be between 01.01.1870 and 01.01.2000 in the format dd.MM.yyyy")
  @NotNull(message = "Birthday is required")
  @Column(name = "birthday")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthday;
  @NotBlank(message = "Nationality cannot be empty")
  @Column(name = "nationality")
  private String nationality;
  @NotBlank(message = "Last location cannot be empty")
  @Column(name = "last_location")
  private String lastLocation;
  @NotBlank(message = "Language cannot be empty")
  @Column(name = "languages")
  private String languages;
  @NotNull(message = "Main criminal profession cannot be empty")
  @Column(name = "main_criminal_profession")
  @Enumerated(EnumType.STRING)
  private CriminalProfession mainCriminalProfession;
  @NotBlank(message = "Description profession cannot be empty")
  @Column(name = "description")
  private String description;
  @NotNull(message = "Status cannot be empty")
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private OffenderStatus status;
  @NotNull(message = "Leave status cannot be empty")
  @Column(name = "leave_status")
  private Boolean leave;
  @Column(name = "archived")
  private Boolean archived;

  @Column(name = "photo", columnDefinition = "LONGBLOB")
  @Lob
  private byte[] photo;

  @ManyToOne
  @JoinColumn(name = "gang_id")
  private CriminalGang gang;

  @ManyToMany(mappedBy = "offenders")
  private Set<Crime> crimes = new HashSet<>();

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
