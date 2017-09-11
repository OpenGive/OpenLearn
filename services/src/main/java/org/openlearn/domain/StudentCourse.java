package org.openlearn.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * An entity representing one student's enrollment in one course
 */
@Entity
@Table(name = "student_course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentCourse implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "student_id")
	private User student;

	@ManyToOne(optional = false)
	@JoinColumn(name = "course_id")
	private Course course;

	@Column(name = "grade", length = 20, nullable = false)
	private String grade = "-";

	@Column(name = "enroll_date", nullable = false)
	private ZonedDateTime enrollDate = ZonedDateTime.now();

	@Column(name = "drop_date")
	private ZonedDateTime dropDate;

	@Column(name = "complete", nullable = false)
	private Boolean complete = false;

	@Column(name = "on_portfolio", nullable = false)
	private Boolean onPortfolio = false;

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

		StudentCourse that = (StudentCourse) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
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
		return "StudentCourse{" +
			"id=" + id +
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
