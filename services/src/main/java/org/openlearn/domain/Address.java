package org.openlearn.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.openlearn.domain.enumeration.State;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 5, max = 50)
	@Column(name = "street_address_1", length = 50, nullable = false)
	private String streetAddress1;

	@Size(min = 5, max = 50)
	@Column(name = "street_address_2", length = 50)
	private String streetAddress2;

	@NotNull
	@Size(max = 50)
	@Column(name = "city", length = 50, nullable = false)
	private String city;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "state", nullable = false)
	private State state;

	@NotNull
	@Size(min = 5, max = 10)
	@Column(name = "postal_code", length = 10, nullable = false)
	private String postalCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreetAddress1() {
		return streetAddress1;
	}

	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}

	public String getStreetAddress2() {
		return streetAddress2;
	}

	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public boolean isEmpty() {
		return ((getStreetAddress1() == null || getStreetAddress1().isEmpty()) &&
			(getStreetAddress2() == null || getStreetAddress2().isEmpty()) &&
			(getCity() == null || getCity().isEmpty()) &&
			(getPostalCode() == null || getPostalCode().isEmpty())
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Address address = (Address) o;

		if (id != null ? !id.equals(address.id) : address.id != null) return false;
		if (!streetAddress1.equals(address.streetAddress1)) return false;
		if (streetAddress2 != null ? !streetAddress2.equals(address.streetAddress2) : address.streetAddress2 != null)
			return false;
		if (!city.equals(address.city)) return false;
		if (state != address.state) return false;
		return postalCode.equals(address.postalCode);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + streetAddress1.hashCode();
		result = 31 * result + (streetAddress2 != null ? streetAddress2.hashCode() : 0);
		result = 31 * result + city.hashCode();
		result = 31 * result + state.hashCode();
		result = 31 * result + postalCode.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Address{" +
			"id=" + id +
			", streetAddress1='" + streetAddress1 + '\'' +
			", streetAddress2='" + streetAddress2 + '\'' +
			", city='" + city + '\'' +
			", state=" + state +
			", postalCode='" + postalCode + '\'' +
			'}';
	}
}
