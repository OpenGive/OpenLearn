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
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "portfolio")
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User student;

    @OneToMany(mappedBy = "portfolio")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ItemLink> portfolioItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public Portfolio student(User user) {
        this.student = user;
        return this;
    }

    public void setStudent(User user) {
        this.student = user;
    }

    public Set<ItemLink> getPortfolioItems() {
        return portfolioItems;
    }

    public Portfolio portfolioItems(Set<ItemLink> itemLinks) {
        this.portfolioItems = itemLinks;
        return this;
    }

    public Portfolio addPortfolioItems(ItemLink itemLink) {
        this.portfolioItems.add(itemLink);
        itemLink.setPortfolio(this);
        return this;
    }

    public Portfolio removePortfolioItems(ItemLink itemLink) {
        this.portfolioItems.remove(itemLink);
        itemLink.setPortfolio(null);
        return this;
    }

    public void setPortfolioItems(Set<ItemLink> itemLinks) {
        this.portfolioItems = itemLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Portfolio portfolio = (Portfolio) o;
        if (portfolio.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, portfolio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Portfolio{" +
            "id=" + id +
            '}';
    }
}
