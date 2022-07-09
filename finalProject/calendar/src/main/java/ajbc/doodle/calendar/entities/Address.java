package ajbc.doodle.calendar.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "addresses")
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addressId;
	private String country;
	private String city;
	private String street;
	private Integer number;
	
	public Address(String country, String city, String street, Integer number) {

		this.country = country;
		this.city = city;
		this.street = street;
		this.number = number;
	}
	
}