package org.openlearn.dto;

import org.openlearn.config.Constants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A DTO representing a user
 */
public class UserDTO {

	private Long id;

	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 100)
	private String login;

	private String password;

	private String firstName;

	private String lastName;

	private String authority;

	private String email;

	private String phoneNumber;

	private String streetAddress1;

	private String streetAddress2;

	private String city;

	private String state;

	private String postalCode;

	private String notes;

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
		this.login = login;
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

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDTO userDTO = (UserDTO) o;

		if (id != null ? !id.equals(userDTO.id) : userDTO.id != null) return false;
		if (login != null ? !login.equals(userDTO.login) : userDTO.login != null) return false;
		if (firstName != null ? !firstName.equals(userDTO.firstName) : userDTO.firstName != null) return false;
		if (lastName != null ? !lastName.equals(userDTO.lastName) : userDTO.lastName != null) return false;
		if (authority != null ? !authority.equals(userDTO.authority) : userDTO.authority != null) return false;
		if (email != null ? !email.equals(userDTO.email) : userDTO.email != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(userDTO.phoneNumber) : userDTO.phoneNumber != null) return false;
		if (streetAddress1 != null ? !streetAddress1.equals(userDTO.streetAddress1) : userDTO.streetAddress1 != null)
			return false;
		if (streetAddress2 != null ? !streetAddress2.equals(userDTO.streetAddress2) : userDTO.streetAddress2 != null)
			return false;
		if (city != null ? !city.equals(userDTO.city) : userDTO.city != null) return false;
		if (state != null ? !state.equals(userDTO.state) : userDTO.state != null) return false;
		if (postalCode != null ? !postalCode.equals(userDTO.postalCode) : userDTO.postalCode != null) return false;
		return notes != null ? notes.equals(userDTO.notes) : userDTO.notes == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (login != null ? login.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (authority != null ? authority.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (streetAddress1 != null ? streetAddress1.hashCode() : 0);
		result = 31 * result + (streetAddress2 != null ? streetAddress2.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
		result = 31 * result + (notes != null ? notes.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserDTO{" +
			"id=" + id +
			", login='" + login + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", authority='" + authority + '\'' +
			", email='" + email + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", streetAddress1='" + streetAddress1 + '\'' +
			", streetAddress2='" + streetAddress2 + '\'' +
			", city='" + city + '\'' +
			", state='" + state + '\'' +
			", postalCode='" + postalCode + '\'' +
			", notes='" + notes + '\'' +
			'}';
	}
}
