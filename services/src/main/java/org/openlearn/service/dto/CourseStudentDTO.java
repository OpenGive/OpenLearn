package org.openlearn.service.dto;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.openlearn.domain.User;
import org.openlearn.domain.CourseStudent;

/**
 * A DTO representing a CourseStudent
 */
public class CourseStudentDTO
{
	private Long courseId;

	private Long userId;

	private UserDTO student;

	private String grade;

	public CourseStudentDTO() {
		// Empty constructor needed for MapStruct.
	}

	public CourseStudentDTO(final CourseStudent courseStudent) {

		this.courseId = courseStudent.getCourseId();
		this.userId = courseStudent.getUserId();
		this.student = new UserDTO(courseStudent.getStudent());
		this.grade = courseStudent.getGrade();

	}

	public Long getCourseId() {
		return courseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setCourseId(Long courseId){
		this.courseId = courseId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public UserDTO getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = new UserDTO(student);
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
	  return "StudentCourse{" +
		  "courseId=" + courseId +
		  ", userId=" + userId +
		  ", student=" + student +
		  ", grade='" + grade + '\'' +
		  '}';
	}
}
