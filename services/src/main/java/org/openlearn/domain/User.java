package org.openlearn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.openlearn.config.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A user.
 */
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 100)
	@Column(length = 100, unique = true, nullable = false)
	private String login;

	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60)
	private String password;

	@NotNull
	@Size(max = 50)
	@Column(length = 50)
	private String firstName;

	@NotNull
	@Size(max = 50)
	@Column(length = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	@Column(length = 100)
	private String email;

	@Size(max = 15)
	@Column(length = 15)
	private String phoneNumber;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "addr_id")
	private Address address;

	@NotNull
	@Column(nullable = false)
	private Boolean fourteenPlus;

	@Size(max = 2000)
	@Column(length = 2000)
	private String biography;

	@ManyToOne(optional = false)
	@NotNull
	private Authority authority;

	@ManyToOne(optional = false)
	private Organization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login.toLowerCase(Locale.US);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Boolean isFourteenPlus() {
		return fourteenPlus;
	}

	public void setFourteenPlus(Boolean fourteenPlus) {
		this.fourteenPlus = fourteenPlus;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (id != null ? !id.equals(user.id) : user.id != null) return false;
		if (!login.equals(user.login)) return false;
		if (!password.equals(user.password)) return false;
		if (!firstName.equals(user.firstName)) return false;
		if (!lastName.equals(user.lastName)) return false;
		if (email != null ? !email.equals(user.email) : user.email != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
		if (address != null ? !address.equals(user.address) : user.address != null) return false;
		if (!fourteenPlus.equals(user.fourteenPlus)) return false;
		if (biography != null ? !biography.equals(user.biography) : user.biography != null) return false;
		if (!authority.equals(user.authority)) return false;
		return organization != null ? organization.equals(user.organization) : user.organization == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + login.hashCode();
		result = 31 * result + password.hashCode();
		result = 31 * result + firstName.hashCode();
		result = 31 * result + lastName.hashCode();
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + fourteenPlus.hashCode();
		result = 31 * result + (biography != null ? biography.hashCode() : 0);
		result = 31 * result + authority.hashCode();
		result = 31 * result + (organization != null ? organization.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", login='" + login + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", email='" + email + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", address=" + address +
			", fourteenPlus=" + fourteenPlus +
			", biography='" + biography + '\'' +
			", authority=" + authority +
			", organization=" + organization +
			'}';
	}
}
