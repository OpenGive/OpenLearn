package org.openlearn.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 3, max = 100)
	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Size(min = 10, max = 800)
	@Column(name = "description", length = 800)
	private String description;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="user_org", joinColumns = @JoinColumn(name = "org_id", referencedColumnName = "id"))
	@Column(name = "user_id")
	public Set<Long> userIds;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Organization name(final String name) {
		this.name = name;
		return this;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public Organization description(final String description) {
		this.description = description;
		return this;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Set<Long> getUserIds() {
		return userIds;
	}

	public Organization userIds(final Set<Long> userIds) {
		this.userIds = userIds;
		return this;
	}

	public Organization addUsers(final Long userId) {
		userIds.add(userId);
		return this;
	}

	public Organization removeUsers(final Long userId) {
		userIds.remove(userId);
		return this;
	}

	public void setUserIds(final Set<Long> userIds) {
		this.userIds = userIds;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Organization organization = (Organization) o;
		if (organization.id == null || id == null)
			return false;
		return Objects.equals(id, organization.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Organization{" +
				"id=" + id +
				", name='" + name + "'" +
				", description='" + description + "'" +
				'}';
	}
}
