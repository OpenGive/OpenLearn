package org.openlearn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
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
	@JoinColumn(name = "program_id")
	private Program program;

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

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
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

		Session session = (Session) o;

		if (id != null ? !id.equals(session.id) : session.id != null) return false;
		if (name != null ? !name.equals(session.name) : session.name != null) return false;
		if (description != null ? !description.equals(session.description) : session.description != null) return false;
		if (startDate != null ? !startDate.equals(session.startDate) : session.startDate != null) return false;
		if (endDate != null ? !endDate.equals(session.endDate) : session.endDate != null) return false;
		if (program != null ? !program.equals(session.program) : session.program != null) return false;
		return organization != null ? organization.equals(session.organization) : session.organization == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		result = 31 * result + (program != null ? program.hashCode() : 0);
		result = 31 * result + (organization != null ? organization.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Session{" +
			"id=" + id +
			", name='" + name + '\'' +
			", description='" + description + '\'' +
			", startDate=" + startDate +
			", endDate=" + endDate +
			", program=" + program +
			", organization=" + organization +
			'}';
	}
}
