package ajbc.doodle.calendar.entities;

import ajbc.doodle.calendar.enums.Category;
import ajbc.doodle.calendar.enums.RepeatingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	
	@JsonIgnore
	@Column(updatable = false, insertable = false)
	private Integer eventOwnerId;
	@ManyToOne
	@JoinColumn(name = "eventOwnerId")
	private User eventOwner;
	
	private String title;
	
	@Enumerated(EnumType.STRING)
	private Category category;
	
	@JsonIgnore
	@Column(insertable = false, updatable = false)
	private Integer addressId;
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="addressId")
	private Address address;
	
	private Integer isAllDay;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private RepeatingType repeatingType;
	
	private Integer inActive;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@JsonProperty(access = Access.WRITE_ONLY)
	@JoinTable(name = "usersEvents", joinColumns = @JoinColumn(name = "eventId"),
	           inverseJoinColumns = @JoinColumn(name = "userId"))
	private Set<User> eventGuests = new HashSet<User>();
	

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "eventToNotify", cascade = { CascadeType.MERGE })
	@JsonProperty(access = Access.READ_ONLY)
	private Set<Notification> notifications = new HashSet<Notification>();
	
	public Event(User eventOwner, String title, Category category, Integer addressId, Integer isAllDay, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String description, RepeatingType repeatingType, Set<User> eventGuests) {
		this.eventOwner = eventOwner;
		this.title = title;
		this.category= category;
		this.addressId=addressId;
		this.isAllDay = isAllDay;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.description = description;
		this.repeatingType = repeatingType;
		this.inActive = 0;
		this.eventGuests = eventGuests;
	}
	
}