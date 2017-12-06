package org.openlearn.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * A DTO representing a course
 */
public class CourseDTO {

	private Long id;

	@NotNull
	@Size(min = 3, max = 100)
	private String name;

	@NotNull
	@Size(min = 5, max = 200)
	private String description;

	@NotNull
	private ZonedDateTime startDate;

	@NotNull
	private ZonedDateTime endDate;

	@NotNull
	private Long sessionId;

	@NotNull
	private Long instructorId;

	private String locations;

	private String times;

	private SessionDTO session;

	private InstructorDTO instructor;

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

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(Long instructorId) {
		this.instructorId = instructorId;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public SessionDTO getSession() { return session; }

	public void setSession(SessionDTO session) { this.session = session; }

	public InstructorDTO getInstructor() { return instructor; }

	public void setInstructor(InstructorDTO instructor) { this.instructor = instructor; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CourseDTO courseDTO = (CourseDTO) o;

		if (id != null ? !id.equals(courseDTO.id) : courseDTO.id != null) return false;
		if (name != null ? !name.equals(courseDTO.name) : courseDTO.name != null) return false;
		if (description != null ? !description.equals(courseDTO.description) : courseDTO.description != null)
			return false;
		if (startDate != null ? !startDate.equals(courseDTO.startDate) : courseDTO.startDate != null) return false;
		if (endDate != null ? !endDate.equals(courseDTO.endDate) : courseDTO.endDate != null) return false;
		if (sessionId != null ? !sessionId.equals(courseDTO.sessionId) : courseDTO.sessionId != null) return false;
		if (instructorId != null ? !instructorId.equals(courseDTO.instructorId) : courseDTO.instructorId != null)
			return false;
		if (locations != null ? !locations.equals(courseDTO.locations) : courseDTO.locations != null) return false;
		return times != null ? times.equals(courseDTO.times) : courseDTO.times == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
		result = 31 * result + (instructorId != null ? instructorId.hashCode() : 0);
		result = 31 * result + (locations != null ? locations.hashCode() : 0);
		result = 31 * result + (times != null ? times.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CourseDTO{" +
			"id=" + id +
			", name='" + name + '\'' +
			", description='" + description + '\'' +
			", startDate=" + startDate +
			", endDate=" + endDate +
			", sessionId=" + sessionId +
			", instructorId=" + instructorId +
			", locations='" + locations + '\'' +
			", times='" + times + '\'' +
			'}';
	}
}
