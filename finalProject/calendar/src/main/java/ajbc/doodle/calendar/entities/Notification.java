package ajbc.doodle.calendar.entities;

import ajbc.doodle.calendar.enums.Unit;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;
	private String title;
	private String message;
	@Enumerated(EnumType.STRING)
	private Unit unit;
	private Integer quantity;
	
	private Integer inActive;
	private Integer isSent;
	
	@Column(insertable = false, updatable = false)
	private Integer eventId;
	@ManyToOne
    @JoinColumn(name="eventId")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Event eventToNotify;
	
	@Column(insertable = false, updatable = false)
	private Integer userId;
	@ManyToOne
    @JoinColumn(name="userId")
	@JsonProperty(access = Access.WRITE_ONLY)
	private User userToNotify;
	
	public Notification(String title, String message, Unit unit, Integer quantity, Event eventToNotify, User userToNotify) {
		this.title = title;
		this.message = message;
		this.unit = unit;
		this.quantity = quantity;
		this.eventToNotify = eventToNotify;
		this.userToNotify = userToNotify;
		this.inActive=0;
		this.isSent=0;

	}
	

}