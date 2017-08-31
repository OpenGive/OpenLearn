package org.openlearn.service.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.openlearn.config.Constants;
import org.openlearn.domain.Address;
import org.openlearn.domain.Authority;
import org.openlearn.domain.Organization;
import org.openlearn.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO
{

	private Long id;

	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 100)
	private String login;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	@Size(min = 10, max = 15)
	private String phoneNumber;

	private Address address;

	private boolean fourteenPlus;

	private String biography;

	private String authority;

	private Long organizationId;

	public UserDTO() {
	}

	public UserDTO(Long id, String login, String firstName, String lastName, String email, String phoneNumber,
	               Address address, boolean fourteenPlus, String biography, String authority, Long organizationId) {
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.fourteenPlus = fourteenPlus;
		this.biography = biography;
		this.authority = authority;
		this.organizationId = organizationId;
	}

	public UserDTO(final User user) {
		this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail(),
			user.getPhoneNumber(), user.getAddress(), user.isFourteenPlus(), user.getBiography(),
			user.getAuthority().getName(), user.getOrganization().getId());
	}

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

	public boolean isFourteenPlus() {
		return fourteenPlus;
	}

	public void setFourteenPlus(boolean fourteenPlus) {
		this.fourteenPlus = fourteenPlus;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDTO userDTO = (UserDTO) o;

		if (fourteenPlus != userDTO.fourteenPlus) return false;
		if (id != null ? !id.equals(userDTO.id) : userDTO.id != null) return false;
		if (login != null ? !login.equals(userDTO.login) : userDTO.login != null) return false;
		if (firstName != null ? !firstName.equals(userDTO.firstName) : userDTO.firstName != null) return false;
		if (lastName != null ? !lastName.equals(userDTO.lastName) : userDTO.lastName != null) return false;
		if (email != null ? !email.equals(userDTO.email) : userDTO.email != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(userDTO.phoneNumber) : userDTO.phoneNumber != null) return false;
		if (address != null ? !address.equals(userDTO.address) : userDTO.address != null) return false;
		if (biography != null ? !biography.equals(userDTO.biography) : userDTO.biography != null) return false;
		if (authority != null ? !authority.equals(userDTO.authority) : userDTO.authority != null) return false;
		return organizationId != null ? organizationId.equals(userDTO.organizationId) : userDTO.organizationId == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (login != null ? login.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (fourteenPlus ? 1 : 0);
		result = 31 * result + (biography != null ? biography.hashCode() : 0);
		result = 31 * result + (authority != null ? authority.hashCode() : 0);
		result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserDTO{" +
			"id=" + id +
			", login='" + login + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", email='" + email + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", address=" + address +
			", fourteenPlus=" + fourteenPlus +
			", biography='" + biography + '\'' +
			", authority='" + authority + '\'' +
			", organizationId=" + organizationId +
			'}';
	}
}
