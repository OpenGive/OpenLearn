package org.openlearn.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * An entity representing one student's relationship to one assignment
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentAssignment {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(optional = false)
	private User student;

	@NotNull
	@ManyToOne(optional = false)
	private Assignment assignment;

	@NotNull
	@ManyToOne(optional = false)
	private String grade;

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

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

		StudentAssignment that = (StudentAssignment) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (student != null ? !student.equals(that.student) : that.student != null) return false;
		if (assignment != null ? !assignment.equals(that.assignment) : that.assignment != null) return false;
		if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
		return onPortfolio != null ? onPortfolio.equals(that.onPortfolio) : that.onPortfolio == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (student != null ? student.hashCode() : 0);
		result = 31 * result + (assignment != null ? assignment.hashCode() : 0);
		result = 31 * result + (grade != null ? grade.hashCode() : 0);
		result = 31 * result + (onPortfolio != null ? onPortfolio.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "StudentAssignment{" +
			"id=" + id +
			", student=" + student +
			", assignment=" + assignment +
			", grade='" + grade + '\'' +
			", onPortfolio=" + onPortfolio +
			'}';
	}
}
