package org.openlearn.domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

	@NotNull
	@Size(min = 10, max = 800)
	@Column(name = "description", length = 800, nullable = false)
	private String description;

	@NotNull
	@Column(name = "primary_contact_method", nullable = false)
	private String primaryContactName;

	@NotNull
	@Column(name = "primary_contact_info", nullable = false)
	private String primaryContactInfo;

	@Column(name = "secondary_contact_name")
	private String secondaryContactName;

	@Column(name = "secondary_contact_info")
	private String secondaryContactInfo;

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

	public String getPrimaryContactName() {
		return primaryContactName;
	}

	public void setPrimaryContactName(String primaryContactName) {
		this.primaryContactName = primaryContactName;
	}

	public String getPrimaryContactInfo() {
		return primaryContactInfo;
	}

	public void setPrimaryContactInfo(String primaryContactInfo) {
		this.primaryContactInfo = primaryContactInfo;
	}

	public String getSecondaryContactName() {
		return secondaryContactName;
	}

	public void setSecondaryContactName(String secondaryContactName) {
		this.secondaryContactName = secondaryContactName;
	}

	public String getSecondaryContactInfo() {
		return secondaryContactInfo;
	}

	public void setSecondaryContactInfo(String secondaryContactInfo) {
		this.secondaryContactInfo = secondaryContactInfo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Organization that = (Organization) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;
		if (primaryContactName != null ? !primaryContactName.equals(that.primaryContactName) : that.primaryContactName != null)
			return false;
		if (primaryContactInfo != null ? !primaryContactInfo.equals(that.primaryContactInfo) : that.primaryContactInfo != null)
			return false;
		if (secondaryContactName != null ? !secondaryContactName.equals(that.secondaryContactName) : that.secondaryContactName != null)
			return false;
		return secondaryContactInfo != null ? secondaryContactInfo.equals(that.secondaryContactInfo) : that.secondaryContactInfo == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (primaryContactName != null ? primaryContactName.hashCode() : 0);
		result = 31 * result + (primaryContactInfo != null ? primaryContactInfo.hashCode() : 0);
		result = 31 * result + (secondaryContactName != null ? secondaryContactName.hashCode() : 0);
		result = 31 * result + (secondaryContactInfo != null ? secondaryContactInfo.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Organization{" +
			"id=" + id +
			", name='" + name + '\'' +
			", description='" + description + '\'' +
			", primaryContactName='" + primaryContactName + '\'' +
			", primaryContactInfo='" + primaryContactInfo + '\'' +
			", secondaryContactName='" + secondaryContactName + '\'' +
			", secondaryContactInfo='" + secondaryContactInfo + '\'' +
			'}';
	}
}
