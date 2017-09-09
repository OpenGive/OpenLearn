package org.openlearn.dto;

/**
 * A DTO representing a student user
 */
public class StudentDTO extends UserDTO {

	private Long organizationId;

	private Boolean fourteenPlus;

	private String guardianFirstName;

	private String guardianLastName;

	private String guardianEmail;

	private String guardianPhone;

	private String school;

	private String gradeLevel;

	private String stateStudentId;

	private String orgStudentId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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

	public String getGuardianEmail() {
		return guardianEmail;
	}

	public void setGuardianEmail(String guardianEmail) {
		this.guardianEmail = guardianEmail;
	}

	public String getGuardianPhone() {
		return guardianPhone;
	}

	public void setGuardianPhone(String guardianPhone) {
		this.guardianPhone = guardianPhone;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(String gradeLevel) {
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
		if (!super.equals(o)) return false;

		StudentDTO that = (StudentDTO) o;

		if (organizationId != null ? !organizationId.equals(that.organizationId) : that.organizationId != null)
			return false;
		if (fourteenPlus != null ? !fourteenPlus.equals(that.fourteenPlus) : that.fourteenPlus != null) return false;
		if (guardianFirstName != null ? !guardianFirstName.equals(that.guardianFirstName) : that.guardianFirstName != null)
			return false;
		if (guardianLastName != null ? !guardianLastName.equals(that.guardianLastName) : that.guardianLastName != null)
			return false;
		if (guardianEmail != null ? !guardianEmail.equals(that.guardianEmail) : that.guardianEmail != null)
			return false;
		if (guardianPhone != null ? !guardianPhone.equals(that.guardianPhone) : that.guardianPhone != null)
			return false;
		if (school != null ? !school.equals(that.school) : that.school != null) return false;
		if (gradeLevel != null ? !gradeLevel.equals(that.gradeLevel) : that.gradeLevel != null) return false;
		if (stateStudentId != null ? !stateStudentId.equals(that.stateStudentId) : that.stateStudentId != null)
			return false;
		return orgStudentId != null ? orgStudentId.equals(that.orgStudentId) : that.orgStudentId == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
		result = 31 * result + (fourteenPlus != null ? fourteenPlus.hashCode() : 0);
		result = 31 * result + (guardianFirstName != null ? guardianFirstName.hashCode() : 0);
		result = 31 * result + (guardianLastName != null ? guardianLastName.hashCode() : 0);
		result = 31 * result + (guardianEmail != null ? guardianEmail.hashCode() : 0);
		result = 31 * result + (guardianPhone != null ? guardianPhone.hashCode() : 0);
		result = 31 * result + (school != null ? school.hashCode() : 0);
		result = 31 * result + (gradeLevel != null ? gradeLevel.hashCode() : 0);
		result = 31 * result + (stateStudentId != null ? stateStudentId.hashCode() : 0);
		result = 31 * result + (orgStudentId != null ? orgStudentId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "StudentDTO{" +
			"organizationId=" + organizationId +
			", fourteenPlus=" + fourteenPlus +
			", guardianFirstName='" + guardianFirstName + '\'' +
			", guardianLastName='" + guardianLastName + '\'' +
			", guardianEmail='" + guardianEmail + '\'' +
			", guardianPhone='" + guardianPhone + '\'' +
			", school='" + school + '\'' +
			", gradeLevel='" + gradeLevel + '\'' +
			", stateStudentId='" + stateStudentId + '\'' +
			", orgStudentId='" + orgStudentId + '\'' +
			"} " + super.toString();
	}
}
