package it.univaq.sose.mangastoreaccountservice.repository.dao;

import it.univaq.sose.mangastorecommons.util.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author: Adam Bouafia, Date : 07-01-2024
 */
/**
 * Represents a role in the application.
 * A role defines the permissions and access rights of a user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLE")
@Builder
public class Role extends DateAudit {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "ROLE_ID", updatable = false, nullable = false)
  private String id;

  @Column(name = "ROLE_NAME", nullable = false)
  private String roleName;

  @ManyToMany(fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "roles")
  @JsonIgnore
  private Set<User> users = new HashSet<>();

  @Column(name = "ROLE_DESCRIPTION")
  private String roleDescription;

  public void addUser(User user) {
    this.users.add(user);
    user.getRoles().add(this);
  }

  public void removeUser(User user) {
    this.users.remove(user);
    user.getRoles().remove(this);
  }
}
