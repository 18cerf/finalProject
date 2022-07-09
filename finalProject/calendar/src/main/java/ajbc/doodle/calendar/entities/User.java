package ajbc.doodle.calendar.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer userId;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private LocalDate birthDate;
    private LocalDate joinDate;
    private Integer inActive;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "eventGuests")
    private Set<Event> events = new HashSet<Event>();


    public User(String firstName, String lastName, String email, LocalDate birthDate, LocalDate joinDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.inActive = 0;
    }

}