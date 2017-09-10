package org.openlearn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

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
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Course implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 3, max = 100)
	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@NotNull
	@Size(min = 5, max = 200)
	@Column(name = "description", length = 200, nullable = false)
	private String description;

	@NotNull
	@Column(name = "start_date", nullable = false)
	private ZonedDateTime startDate;

	@NotNull
	@Column(name = "end_date", nullable = false)
	private ZonedDateTime endDate;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "session_id")
	private Session session;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "instructor_id")
	private User instructor;

	@Column(name = "locations")
	private String locations;

	@Column(name = "times")
	private String times;

	@ManyToOne(optional = false)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public User getInstructor() {
		return instructor;
	}

	public void setInstructor(User instructor) {
		this.instructor = instructor;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Course course = (Course) o;

		if (id != null ? !id.equals(course.id) : course.id != null) return false;
		if (name != null ? !name.equals(course.name) : course.name != null) return false;
		if (description != null ? !description.equals(course.description) : course.description != null) return false;
		if (startDate != null ? !startDate.equals(course.startDate) : course.startDate != null) return false;
		if (endDate != null ? !endDate.equals(course.endDate) : course.endDate != null) return false;
		if (session != null ? !session.equals(course.session) : course.session != null) return false;
		if (instructor != null ? !instructor.equals(course.instructor) : course.instructor != null) return false;
		if (locations != null ? !locations.equals(course.locations) : course.locations != null) return false;
		if (times != null ? !times.equals(course.times) : course.times != null) return false;
		return organization != null ? organization.equals(course.organization) : course.organization == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		result = 31 * result + (session != null ? session.hashCode() : 0);
		result = 31 * result + (instructor != null ? instructor.hashCode() : 0);
		result = 31 * result + (locations != null ? locations.hashCode() : 0);
		result = 31 * result + (times != null ? times.hashCode() : 0);
		result = 31 * result + (organization != null ? organization.hashCode() : 0);
		return result;
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
			", locations='" + locations + '\'' +
			", times='" + times + '\'' +
			", organization=" + organization +
			'}';
	}
}
