package org.openlearn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * A Course.
 */
//TODO: Set the correct @JsonIgnore Fields
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Course implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 3, max = 100)
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
	@ManyToOne(optional = false)
	private Session session;

	@NotNull
	@ManyToOne(optional = false)
	private User instructor;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Course name(final String name) {
		this.name = name;
		return this;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public Course description(final String description) {
		this.description = description;
		return this;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public Course startDate(final ZonedDateTime startDate) {
		this.startDate = startDate;
		return this;
	}

	public void setStartDate(final ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public Course endDate(final ZonedDateTime endDate) {
		this.endDate = endDate;
		return this;
	}

	public void setEndDate(final ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	public User getInstructor() {
		return instructor;
	}

	public Course instructor(final User user) {
		instructor = user;
		return this;
	}

	public void setInstructor(final User user) {
		instructor = user;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Course course = (Course) o;
		if (course.id == null || id == null)
			return false;
		return Objects.equals(id, course.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

  @Override
  public String toString() {
    return "Course{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", startDate=" + startDate +
      ", endDate=" + endDate +
      ", session=" + session +
      ", instructor=" + instructor +
      '}';
  }
}
