package org.openlearn.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StudentAssignmentDTO {

	private Long id;

	@NotNull
	private Long studentId;

	@NotNull
	private Long assignmentId;

	private StudentDTO student;

	private AssignmentDTO assignment;

	@NotNull
	@Size(max = 20)
	private String grade;

	private Boolean complete;

	private Boolean onPortfolio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public StudentDTO getStudent() {
		return student;
	}

	public void setStudent(StudentDTO student) {
		this.student = student;
	}

	public AssignmentDTO getAssignment() {
		return assignment;
	}

	public void setAssignment(AssignmentDTO assignment) {
		this.assignment = assignment;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Boolean getComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	public Boolean getOnPortfolio() {
		return onPortfolio;
	}

	public void setOnPortfolio(Boolean onPortfolio) {
		this.onPortfolio = onPortfolio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StudentAssignmentDTO that = (StudentAssignmentDTO) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (studentId != null ? !studentId.equals(that.studentId) : that.studentId != null) return false;
		if (assignmentId != null ? !assignmentId.equals(that.assignmentId) : that.assignmentId != null) return false;
		if (student != null ? !student.equals(that.student) : that.student != null) return false;
		if (assignment != null ? !assignment.equals(that.assignment) : that.assignment != null) return false;
		if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
		if (complete != null ? !complete.equals(that.complete) : that.complete != null) return false;
		return onPortfolio != null ? onPortfolio.equals(that.onPortfolio) : that.onPortfolio == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
		result = 31 * result + (assignmentId != null ? assignmentId.hashCode() : 0);
		result = 31 * result + (student != null ? student.hashCode() : 0);
		result = 31 * result + (assignment != null ? assignment.hashCode() : 0);
		result = 31 * result + (grade != null ? grade.hashCode() : 0);
		result = 31 * result + (complete != null ? complete.hashCode() : 0);
		result = 31 * result + (onPortfolio != null ? onPortfolio.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "StudentAssignmentDTO{" +
			"id=" + id +
			", studentId=" + studentId +
			", assignmentId=" + assignmentId +
			", student=" + student +
			", assignment=" + assignment +
			", grade='" + grade + '\'' +
			", complete=" + complete +
			", onPortfolio=" + onPortfolio +
			'}';
	}
}
