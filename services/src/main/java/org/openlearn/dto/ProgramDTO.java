package org.openlearn.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProgramDTO {

	private Long id;

	@NotNull
	@Size(min = 5, max = 50)
	private String name;

	@NotNull
	private String description;

	@NotNull
	private Long organizationId;

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProgramDTO that = (ProgramDTO) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;
		return organizationId != null ? organizationId.equals(that.organizationId) : that.organizationId == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProgramDTO{" +
			"id=" + id +
			", name='" + name + '\'' +
			", description='" + description + '\'' +
			", organizationId=" + organizationId +
			'}';
	}
}
