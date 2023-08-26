package org.file.cabinet.interpol.file.cabinet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

  @Column(name = "gang_danger")
  private Boolean gangDanger;
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

