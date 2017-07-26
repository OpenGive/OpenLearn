package org.openlearn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Session implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 5, max = 100)
	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Size(min = 5, max = 200)
	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "start_date")
	private ZonedDateTime startDate;

	@Column(name = "end_date")
	private ZonedDateTime endDate;

	@NotNull
	@Column(name = "active", nullable = false)
	private Boolean active;

	@ManyToOne
	private School school;

	@ManyToOne(optional = false)
	private Program program;

	@OneToMany(mappedBy = "session")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Course> courses = new HashSet<>();


	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Session name(final String name) {
		this.name = name;
		return this;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(final Program program) {
		this.program = program;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Boolean isActive() {
		return active;
	}

	public Session active(final Boolean active) {
		this.active = active;
		return this;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public Session courses(final Set<Course> courses) {
		this.courses = courses;
		return this;
	}

	public Session addCourse(final Course course) {
		courses.add(course);
		course.setSession(this);
		return this;
	}

	public Session removeCourse(final Course course) {
		courses.remove(course);
		course.setSession(null);
		return this;
	}

	public void setCourses(final Set<Course> courses) {
		this.courses = courses;
	}

	public School getSchool() {
		return school;
	}

	public Session school(final School school) {
		this.school = school;
		return this;
	}

	public void setSchool(final School school) {
		this.school = school;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(final ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(final ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Session session = (Session) o;
		if (session.id == null || id == null)
			return false;
		return Objects.equals(id, session.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Session{" +
				"id=" + id +
				", name='" + name + "'" +
				", active='" + active + "'" +
				'}';
	}
}
