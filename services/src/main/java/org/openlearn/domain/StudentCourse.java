package org.openlearn.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * An entity representing one student's enrollment in one course
 */
@Entity
public class StudentCourse implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(optional = false)
	private User student;

	@NotNull
	@ManyToOne(optional = false)
	private Course course;

	@NotNull
	@Column(nullable = false)
	private String grade;

	@NotNull
	@Column(nullable = false)
	private Boolean complete;

	@NotNull
	@Column(nullable = false)
	private ZonedDateTime enrollDate;

	private ZonedDateTime dropDate;

	private Boolean onPortfolio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
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

		StudentCourse that = (StudentCourse) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (student != null ? !student.equals(that.student) : that.student != null) return false;
		if (course != null ? !course.equals(that.course) : that.course != null) return false;
		if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
		if (complete != null ? !complete.equals(that.complete) : that.complete != null) return false;
		if (enrollDate != null ? !enrollDate.equals(that.enrollDate) : that.enrollDate != null) return false;
		if (dropDate != null ? !dropDate.equals(that.dropDate) : that.dropDate != null) return false;
		return onPortfolio != null ? onPortfolio.equals(that.onPortfolio) : that.onPortfolio == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (student != null ? student.hashCode() : 0);
		result = 31 * result + (course != null ? course.hashCode() : 0);
		result = 31 * result + (grade != null ? grade.hashCode() : 0);
		result = 31 * result + (complete != null ? complete.hashCode() : 0);
		result = 31 * result + (enrollDate != null ? enrollDate.hashCode() : 0);
		result = 31 * result + (dropDate != null ? dropDate.hashCode() : 0);
		result = 31 * result + (onPortfolio != null ? onPortfolio.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "StudentCourse{" +
			"id=" + id +
			", student=" + student +
			", course=" + course +
			", grade='" + grade + '\'' +
			", complete=" + complete +
			", enrollDate=" + enrollDate +
			", dropDate=" + dropDate +
			", onPortfolio=" + onPortfolio +
			'}';
	}
}
