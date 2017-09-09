package org.openlearn.dto;

import javax.validation.constraints.NotNull;

/**
 * A DTO representing account info for a user
 */
public class AccountDTO {

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	private String email;

	private String phoneNumber;

	private String streetAddress1;

	private String streetAddress2;

	private String city;

	private String state;

	private String postalCode;

	private String notes;

	private String orgRole;

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

	public String getOrgRole() {
		return orgRole;
	}

	public void setOrgRole(String orgRole) {
		this.orgRole = orgRole;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AccountDTO that = (AccountDTO) o;

		if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
		if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
		if (streetAddress1 != null ? !streetAddress1.equals(that.streetAddress1) : that.streetAddress1 != null)
			return false;
		if (streetAddress2 != null ? !streetAddress2.equals(that.streetAddress2) : that.streetAddress2 != null)
			return false;
		if (city != null ? !city.equals(that.city) : that.city != null) return false;
		if (state != null ? !state.equals(that.state) : that.state != null) return false;
		if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) return false;
		if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
		return orgRole != null ? orgRole.equals(that.orgRole) : that.orgRole == null;
	}

	@Override
	public int hashCode() {
		int result = firstName != null ? firstName.hashCode() : 0;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (streetAddress1 != null ? streetAddress1.hashCode() : 0);
		result = 31 * result + (streetAddress2 != null ? streetAddress2.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
		result = 31 * result + (notes != null ? notes.hashCode() : 0);
		result = 31 * result + (orgRole != null ? orgRole.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "AccountDTO{" +
			"firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", email='" + email + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", streetAddress1='" + streetAddress1 + '\'' +
			", streetAddress2='" + streetAddress2 + '\'' +
			", city='" + city + '\'' +
			", state='" + state + '\'' +
			", postalCode='" + postalCode + '\'' +
			", notes='" + notes + '\'' +
			", orgRole='" + orgRole + '\'' +
			'}';
	}
}
