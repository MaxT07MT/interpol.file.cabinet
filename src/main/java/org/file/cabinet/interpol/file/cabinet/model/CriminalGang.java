package org.file.cabinet.interpol.file.cabinet.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "criminal_gang")
@AllArgsConstructor
@NoArgsConstructor
public class CriminalGang {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "name")
  private String name;
  @Column(name = "location")
  private String location;

  @Column(name = "year_of_foundation")
  @DateTimeFormat(pattern = "yyyy")
  private Date yearOfFoundation;
  @Column(name = "description")
  private String description;

  @Column(name = "gang_archived")
  private Boolean gangArchived;

  @Column(name = "logo", columnDefinition = "LONGBLOB")
  @Lob
  private byte[] logo;
  @OneToMany(mappedBy = "gang")
  private Set<Offender> offenders = new HashSet<>();

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

