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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "crime")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "name")
  private String name;
  @Column(name = "place")
  private String place;
  @Column(name = "date_of_crime")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date dateOfCrime;
  @Column(name = "description")
  private String description;
  @Column(name = "crime_archived")
  private Boolean crimeArchived;
  @Column(name = "crime_solved")
  private Boolean crimeSolved;
  @Column(name = "crime_danger")
  private Boolean crimeDanger;
  @Column(name = "photo_crime", columnDefinition = "LONGBLOB")
  @Lob
  private byte[] photoCrime;

  @ManyToMany
  @JoinTable(
      name = "crime_offender",
      joinColumns = @JoinColumn(name = "crime_id"),
      inverseJoinColumns = @JoinColumn(name = "offender_id")
  )
  private Set<Offender> offenders = new HashSet<>();

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
