package org.opengive.denver.stem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "program")
public class Program implements Serializable {

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

    @ManyToOne
    private Organization organization;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User instructor;

    @OneToMany(mappedBy = "program")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ItemLink> resources = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> students = new HashSet<>();

    @OneToMany(mappedBy = "program")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Milestone> milestones = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Program name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Program description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Program startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Program endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Program organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getInstructor() {
        return instructor;
    }

    public Program instructor(User user) {
        this.instructor = user;
        return this;
    }

    public void setInstructor(User user) {
        this.instructor = user;
    }

    public Set<ItemLink> getResources() {
        return resources;
    }

    public Program resources(Set<ItemLink> itemLinks) {
        this.resources = itemLinks;
        return this;
    }

    public Program addResources(ItemLink itemLink) {
        this.resources.add(itemLink);
        itemLink.setProgram(this);
        return this;
    }

    public Program removeResources(ItemLink itemLink) {
        this.resources.remove(itemLink);
        itemLink.setProgram(null);
        return this;
    }

    public void setResources(Set<ItemLink> itemLinks) {
        this.resources = itemLinks;
    }

    public Set<User> getStudents() {
        return students;
    }

    public Program students(Set<User> users) {
        this.students = users;
        return this;
    }

    public Program addStudents(User user) {
        this.students.add(user);
        user.setUser(this);
        return this;
    }

    public Program removeStudents(User user) {
        this.students.remove(user);
        user.setUser(null);
        return this;
    }

    public void setStudents(Set<User> users) {
        this.students = users;
    }

    public Set<Milestone> getMilestones() {
        return milestones;
    }

    public Program milestones(Set<Milestone> milestones) {
        this.milestones = milestones;
        return this;
    }

    public Program addMilestones(Milestone milestone) {
        this.milestones.add(milestone);
        milestone.setProgram(this);
        return this;
    }

    public Program removeMilestones(Milestone milestone) {
        this.milestones.remove(milestone);
        milestone.setProgram(null);
        return this;
    }

    public void setMilestones(Set<Milestone> milestones) {
        this.milestones = milestones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Program program = (Program) o;
        if (program.id == null || id == null) {
            return false;
        }
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
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}
