package org.opengive.denver.stem.domain;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @OneToMany
    @JoinTable(
			name = "STDT_PRGM", 
			joinColumns = {	@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "PRGM_ID", referencedColumnName = "ID") }
			)
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

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Program startDate(final ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(final ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Program endDate(final ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(final ZonedDateTime endDate) {
        this.endDate = endDate;
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

    public User getInstructor() {
        return instructor;
    }

    public Program instructor(final User user) {
        instructor = user;
        return this;
    }

    public void setInstructor(final User user) {
        instructor = user;
    }

    public Set<ItemLink> getResources() {
        return resources;
    }

    public Program resources(final Set<ItemLink> itemLinks) {
        resources = itemLinks;
        return this;
    }

    public Program addResources(final ItemLink itemLink) {
        resources.add(itemLink);
        itemLink.setProgram(this);
        return this;
    }

    public Program removeResources(final ItemLink itemLink) {
        resources.remove(itemLink);
        itemLink.setProgram(null);
        return this;
    }

    public void setResources(final Set<ItemLink> itemLinks) {
        resources = itemLinks;
    }

    public Set<User> getStudents() {
        return students;
    }

    public Program students(final Set<User> users) {
        students = users;
        return this;
    }

    public Program addStudents(final User user) {
        students.add(user);
        return this;
    }

    public Program removeStudents(final User user) {
        students.remove(user);
        return this;
    }

    public void setStudents(final Set<User> users) {
        students = users;
    }

    public Set<Milestone> getMilestones() {
        return milestones;
    }

    public Program milestones(final Set<Milestone> milestones) {
        this.milestones = milestones;
        return this;
    }

    public Program addMilestones(final Milestone milestone) {
        milestones.add(milestone);
        milestone.setProgram(this);
        return this;
    }

    public Program removeMilestones(final Milestone milestone) {
        milestones.remove(milestone);
        milestone.setProgram(null);
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
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}
