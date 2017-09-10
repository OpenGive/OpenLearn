package org.openlearn.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PortfolioItemDTO {

	private Long id;

	@NotNull
	private String name;

	@NotNull
	@Size(max  = 500)
	private String description;

	@NotNull
	private Long studentId;

	private String url;

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

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PortfolioItemDTO that = (PortfolioItemDTO) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;
		if (studentId != null ? !studentId.equals(that.studentId) : that.studentId != null) return false;
		return url != null ? url.equals(that.url) : that.url == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PortfolioItemDTO{" +
			"id=" + id +
			", name='" + name + '\'' +
			", description='" + description + '\'' +
			", studentId='" + studentId + '\'' +
			", url='" + url + '\'' +
			'}';
	}
}
