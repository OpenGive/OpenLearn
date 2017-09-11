package org.openlearn.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public class StudentCourseDTO {

	private Long id;

	@NotNull
	private Long studentId;

	@NotNull
	private Long courseId;

	private StudentDTO student;

	private CourseDTO course;

	@Size(max = 20)
	private String grade;

	private ZonedDateTime enrollDate;

	private ZonedDateTime dropDate;

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

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public StudentDTO getStudent() {
		return student;
	}

	public void setStudent(StudentDTO student) {
		this.student = student;
	}

	public CourseDTO getCourse() {
		return course;
	}

	public void setCourse(CourseDTO course) {
		this.course = course;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public ZonedDateTime getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(ZonedDateTime enrollDate) {
		this.enrollDate = enrollDate;
	}

	public ZonedDateTime getDropDate() {
		return dropDate;
	}

	public void setDropDate(ZonedDateTime dropDate) {
		this.dropDate = dropDate;
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

		StudentCourseDTO that = (StudentCourseDTO) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (studentId != null ? !studentId.equals(that.studentId) : that.studentId != null) return false;
		if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
		if (student != null ? !student.equals(that.student) : that.student != null) return false;
		if (course != null ? !course.equals(that.course) : that.course != null) return false;
		if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
		if (enrollDate != null ? !enrollDate.equals(that.enrollDate) : that.enrollDate != null) return false;
		if (dropDate != null ? !dropDate.equals(that.dropDate) : that.dropDate != null) return false;
		if (complete != null ? !complete.equals(that.complete) : that.complete != null) return false;
		return onPortfolio != null ? onPortfolio.equals(that.onPortfolio) : that.onPortfolio == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
		result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
		result = 31 * result + (student != null ? student.hashCode() : 0);
		result = 31 * result + (course != null ? course.hashCode() : 0);
		result = 31 * result + (grade != null ? grade.hashCode() : 0);
		result = 31 * result + (enrollDate != null ? enrollDate.hashCode() : 0);
		result = 31 * result + (dropDate != null ? dropDate.hashCode() : 0);
		result = 31 * result + (complete != null ? complete.hashCode() : 0);
		result = 31 * result + (onPortfolio != null ? onPortfolio.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "StudentCourseDTO{" +
			"id=" + id +
			", studentId=" + studentId +
			", courseId=" + courseId +
			", student=" + student +
			", course=" + course +
			", grade='" + grade + '\'' +
			", enrollDate=" + enrollDate +
			", dropDate=" + dropDate +
			", complete=" + complete +
			", onPortfolio=" + onPortfolio +
			'}';
	}
}
