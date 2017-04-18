package org.opengive.denver.stem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Achievement.
 */
@Entity
@Table(name = "achievement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "achievement")
public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(min = 10, max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @Size(min = 10, max = 200)
    @Column(name = "badge_url", length = 200)
    private String badgeUrl;

    @ManyToOne
    private Milestone milestone;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> achievedBies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Achievement name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Achievement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public Achievement badgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
        return this;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public Achievement milestone(Milestone milestone) {
        this.milestone = milestone;
        return this;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public Set<User> getAchievedBies() {
        return achievedBies;
    }

    public Achievement achievedBies(Set<User> users) {
        this.achievedBies = users;
        return this;
    }

    public Achievement addAchievedBy(User user) {
        this.achievedBies.add(user);
        user.setUser(this);
        return this;
    }

    public Achievement removeAchievedBy(User user) {
        this.achievedBies.remove(user);
        user.setUser(null);
        return this;
    }

    public void setAchievedBies(Set<User> users) {
        this.achievedBies = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Achievement achievement = (Achievement) o;
        if (achievement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, achievement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Achievement{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", badgeUrl='" + badgeUrl + "'" +
            '}';
    }
}
