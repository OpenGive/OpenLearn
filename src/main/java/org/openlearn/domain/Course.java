package org.openlearn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * A Course.
 */
//TODO: Set the correct @JsonIgnore Fields
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "course")
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
	private Program program;

	@NotNull
	@ManyToOne(optional = false)
	private User instructor;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name="course_link",
			joinColumns=@JoinColumn(name="course_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="link_id", referencedColumnName="id"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<ItemLink> resources = new HashSet<>();

//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(
//			name = "student_course",
//			joinColumns = {	@JoinColumn(name = "course_id", referencedColumnName = "id") },
//			inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }
//			)
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "course")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<StudentCourse> students = new HashSet<>();

	@OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Milestone> milestones = new HashSet<>();

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

	public Program getProgram() {
		return program;
	}

	public void setProgram(final Program program) {
		this.program = program;
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

	public Set<ItemLink> getResources() {
		return resources;
	}

	public Course resources(final Set<ItemLink> itemLinks) {
		resources = itemLinks;
		return this;
	}

	public Course addResources(final ItemLink itemLink) {
		resources.add(itemLink);
		return this;
	}

	public Course removeResources(final ItemLink itemLink) {
		resources.remove(itemLink);
		return this;
	}

	public void setResources(final Set<ItemLink> itemLinks) {
		resources = itemLinks;
	}

	public Set<User> getStudents() {
	  return students.stream().map(StudentCourse::getUser).collect(Collectors.toSet());
	}

	public Course students(final Set<User> users) {
	  setStudents(users);
		return this;
	}

	public Course addStudents(final User user) {
	  students.add(new StudentCourse(this, user));
		return this;
	}

	public Course removeStudents(final User user) {
	  students = students.stream().filter(sc -> !sc.getUser().equals(user)).collect(Collectors.toSet());
		return this;
	}

	public void setStudents(final Set<User> users) {
	  users.forEach(u -> {
	    students.add(new StudentCourse(this, u));
    });
	}

	public Set<Milestone> getMilestones() {
		return milestones;
	}

	public Course milestones(final Set<Milestone> milestones) {
		this.milestones = milestones;
		return this;
	}

	public Course addMilestones(final Milestone milestone) {
		milestones.add(milestone);
    milestone.setCourse(this);
		return this;
	}

	public Course removeMilestones(final Milestone milestone) {
		milestones.remove(milestone);
    milestone.setCourse(null);
		return this;
	}

	public void setMilestones(final Set<Milestone> milestones) {
		this.milestones = milestones;
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
      ", program=" + program +
      ", instructor=" + instructor +
      ", resources=" + resources +
      ", students=" + students +
      ", milestones=" + milestones +
      '}';
  }
}
