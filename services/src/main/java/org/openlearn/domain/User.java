package org.openlearn.domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.openlearn.config.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openlearn.domain.enumeration.GradeLevel;

/**
 * A user.
 */
@Entity
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

	@NotNull
	@JsonIgnore
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60, nullable = false)
	private String password;

	@NotNull
	@Size(max = 50)
	@Column(length = 50, nullable = false)
	private String firstName;

	@NotNull
	@Size(max = 50)
	@Column(length = 50, nullable = false)
	private String lastName;

	@NotNull
	@ManyToOne(optional = false)
	private Authority authority;

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

	@Size(max = 2000)
	@Column(length = 2000)
	private String notes;

	@ManyToOne
	private Organization organization;

	private String orgRole;

	private Boolean fourteenPlus;

	private String guardianFirstName;

	private String guardianLastName;

	private String guardianPhone;

	private String guardianEmail;

	private String school;

	@Enumerated(EnumType.STRING)
	private GradeLevel gradeLevel;

	private String stateStudentId;

	private String orgStudentId;

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

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getOrgRole() {
		return orgRole;
	}

	public void setOrgRole(String orgRole) {
		this.orgRole = orgRole;
	}

	public Boolean getFourteenPlus() {
		return fourteenPlus;
	}

	public void setFourteenPlus(Boolean fourteenPlus) {
		this.fourteenPlus = fourteenPlus;
	}

	public String getGuardianFirstName() {
		return guardianFirstName;
	}

	public void setGuardianFirstName(String guardianFirstName) {
		this.guardianFirstName = guardianFirstName;
	}

	public String getGuardianLastName() {
		return guardianLastName;
	}

	public void setGuardianLastName(String guardianLastName) {
		this.guardianLastName = guardianLastName;
	}

	public String getGuardianPhone() {
		return guardianPhone;
	}

	public void setGuardianPhone(String guardianPhone) {
		this.guardianPhone = guardianPhone;
	}

	public String getGuardianEmail() {
		return guardianEmail;
	}

	public void setGuardianEmail(String guardianEmail) {
		this.guardianEmail = guardianEmail;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public GradeLevel getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(GradeLevel gradeLevel) {
		this.gradeLevel = gradeLevel;
	}

	public String getStateStudentId() {
		return stateStudentId;
	}

	public void setStateStudentId(String stateStudentId) {
		this.stateStudentId = stateStudentId;
	}

	public String getOrgStudentId() {
		return orgStudentId;
	}

	public void setOrgStudentId(String orgStudentId) {
		this.orgStudentId = orgStudentId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (id != null ? !id.equals(user.id) : user.id != null) return false;
		if (login != null ? !login.equals(user.login) : user.login != null) return false;
		if (password != null ? !password.equals(user.password) : user.password != null) return false;
		if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
		if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
		if (authority != null ? !authority.equals(user.authority) : user.authority != null) return false;
		if (email != null ? !email.equals(user.email) : user.email != null) return false;
		if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
		if (address != null ? !address.equals(user.address) : user.address != null) return false;
		if (notes != null ? !notes.equals(user.notes) : user.notes != null) return false;
		if (organization != null ? !organization.equals(user.organization) : user.organization != null) return false;
		if (orgRole != null ? !orgRole.equals(user.orgRole) : user.orgRole != null) return false;
		if (fourteenPlus != null ? !fourteenPlus.equals(user.fourteenPlus) : user.fourteenPlus != null) return false;
		if (guardianFirstName != null ? !guardianFirstName.equals(user.guardianFirstName) : user.guardianFirstName != null)
			return false;
		if (guardianLastName != null ? !guardianLastName.equals(user.guardianLastName) : user.guardianLastName != null)
			return false;
		if (guardianPhone != null ? !guardianPhone.equals(user.guardianPhone) : user.guardianPhone != null)
			return false;
		if (guardianEmail != null ? !guardianEmail.equals(user.guardianEmail) : user.guardianEmail != null)
			return false;
		if (school != null ? !school.equals(user.school) : user.school != null) return false;
		if (gradeLevel != user.gradeLevel) return false;
		if (stateStudentId != null ? !stateStudentId.equals(user.stateStudentId) : user.stateStudentId != null)
			return false;
		return orgStudentId != null ? orgStudentId.equals(user.orgStudentId) : user.orgStudentId == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (login != null ? login.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (authority != null ? authority.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (notes != null ? notes.hashCode() : 0);
		result = 31 * result + (organization != null ? organization.hashCode() : 0);
		result = 31 * result + (orgRole != null ? orgRole.hashCode() : 0);
		result = 31 * result + (fourteenPlus != null ? fourteenPlus.hashCode() : 0);
		result = 31 * result + (guardianFirstName != null ? guardianFirstName.hashCode() : 0);
		result = 31 * result + (guardianLastName != null ? guardianLastName.hashCode() : 0);
		result = 31 * result + (guardianPhone != null ? guardianPhone.hashCode() : 0);
		result = 31 * result + (guardianEmail != null ? guardianEmail.hashCode() : 0);
		result = 31 * result + (school != null ? school.hashCode() : 0);
		result = 31 * result + (gradeLevel != null ? gradeLevel.hashCode() : 0);
		result = 31 * result + (stateStudentId != null ? stateStudentId.hashCode() : 0);
		result = 31 * result + (orgStudentId != null ? orgStudentId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", login='" + login + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", authority=" + authority +
			", notes='" + notes + '\'' +
			", email='" + email + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", address=" + address +
			", organization=" + organization +
			", orgRole='" + orgRole + '\'' +
			", fourteenPlus=" + fourteenPlus +
			", guardianFirstName='" + guardianFirstName + '\'' +
			", guardianLastName='" + guardianLastName + '\'' +
			", guardianPhone='" + guardianPhone + '\'' +
			", guardianEmail='" + guardianEmail + '\'' +
			", school='" + school + '\'' +
			", gradeLevel=" + gradeLevel +
			", stateStudentId='" + stateStudentId + '\'' +
			", orgStudentId='" + orgStudentId + '\'' +
			'}';
	}
}
