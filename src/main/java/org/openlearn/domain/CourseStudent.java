package org.openlearn.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student_course")
@IdClass(StudentCourseId.class)
public class CourseStudent implements Serializable{

  private static final long serialVersionUID = 1L;

  public CourseStudent(){
  }

  public CourseStudent(Course c, User u){
    userId = u.getId();
    courseId = c.getId();
    student = u;
  }

  @Id
  @Column(name = "course_id",  insertable = false, updatable = false)
  private Long courseId;

  @Id
  @Column(name = "user_id",  insertable = false, updatable = false)
  private Long userId;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
  private User student;

  @Column(name = "grade")
  private String grade;

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

  public User getStudent() {
    return student;
  }

  public void setStudent(User student) {
    this.student = student;
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

  @Override
  public boolean equals(Object o) {
	  if (this == o) return true;
	  if (!(o instanceof CourseStudent)) return false;

	  CourseStudent that = (CourseStudent) o;

	  if (!courseId.equals(that.courseId)) return false;
	  if (!userId.equals(that.userId)) return false;
	  if (student != null ? !student.equals(that.student) : that.student != null) return false;
	  return grade != null ? grade.equals(that.grade) : that.grade == null;
  }

  @Override
  public int hashCode() {
	  int result = courseId.hashCode();
	  result = 31 * result + userId.hashCode();
	  result = 31 * result + (student != null ? student.hashCode() : 0);
	  result = 31 * result + (grade != null ? grade.hashCode() : 0);
	  return result;
  }
}
