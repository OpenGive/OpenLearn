package org.openlearn.domain;

import java.io.Serializable;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Program implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 5, max = 50)
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "active", nullable = false)
	private boolean active;

	@ManyToOne(optional = false)
	@NotNull
	private Organization organization;

	@OneToMany(mappedBy = "program")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Session> sessions = new HashSet<>();


	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Program name(final String name) {
		this.name = name;
		return this;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public Program description(final String description) {
		this.description = description;
		return this;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Boolean isActive() {
		return active;
	}

	public Program active(final Boolean active) {
		this.active = active;
		return this;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Organization getOrganization() {
		return organization;
	}

	public Program organization(final Organization organization) {
		this.organization = organization;
		return this;
	}

	public void setOrganization(final Organization organization) {
		this.organization = organization;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public Program sessions(final Set<Session> sessions) {
		this.sessions = sessions;
		return this;
	}

	public Program addSession(final Session session) {
		sessions.add(session);
		session.setProgram(this);
		return this;
	}

	public Program removeSession(final Session session) {
		sessions.remove(sessions);
		session.setProgram(null);
		return this;
	}

	public void setSessions(final Set<Session> sessions) {
		this.sessions = sessions;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Program program = (Program) o;
		if (program.id == null || id == null)
			return false;
		return Objects.equals(id, program.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Program{" +
				"id=" + id +
				", name='" + name + "'" +
				", description='" + description + "'" +
				", active='" + active + "'" +
				'}';
	}
}
