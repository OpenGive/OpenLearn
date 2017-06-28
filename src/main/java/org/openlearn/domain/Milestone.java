package org.openlearn.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Milestone.
 */
@Entity
@Table(name = "milestone")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "milestone")
public class Milestone implements Serializable {

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

	@NotNull
	@ManyToOne
	private Course course;

  @OneToMany(mappedBy = "milestone")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Achievement> achievements = new HashSet<>();

	@ManyToMany
	@JoinTable(
			name="milestone_link",
			joinColumns=@JoinColumn(name="milestone_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="link_id", referencedColumnName="id"))
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private final Set<ItemLink> resources = new HashSet<>();

	@Column(name = "points")
	@Min(0)
	private Integer points;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Milestone name(final String name) {
		this.name = name;
		return this;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public Milestone description(final String description) {
		this.description = description;
		return this;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Integer getPoints() {
		return points;
	}

	public Milestone points(final Integer points) {
		this.points = points;
		return this;
	}

	public void setPoints(final Integer points) {
		this.points = points;
	}

	public Course getCourse() {
		return course;
	}

	public Milestone course(final Course course) {
		this.course = course;
		return this;
	}

	public void setCourse(final Course course) {
		this.course = course;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Milestone milestone = (Milestone) o;
		if (milestone.id == null || id == null)
			return false;
		return Objects.equals(id, milestone.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Milestone{" +
				"id=" + id +
				", name='" + name + "'" +
				'}';
	}
}
