package org.openlearn.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student_course")
@IdClass(StudentCourseId.class)
public class StudentCourse implements Serializable{

  private static final long serialVersionUID = 1L;

  public StudentCourse(){
  }

  public StudentCourse(Course c, User u){
    userId = u.getId();
    courseId = c.getId();
    user = u;
    course = c;
  }

  @Id
  @Column(name = "course_id",  insertable = false, updatable = false)
  private Long courseId;

  @Id
  @Column(name = "user_id",  insertable = false, updatable = false)
  private Long userId;

  @ManyToOne
//  @JoinColumn(name = "id", insertable = false, updatable = false)
  @PrimaryKeyJoinColumn(name = "course_id", referencedColumnName = "id")
  private Course course;

  @ManyToOne
//  @JoinColumn(name = "id", insertable = false, updatable = false)
  @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name = "grade")
  private String grade;

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StudentCourse)) return false;

    StudentCourse that = (StudentCourse) o;

    if (!courseId.equals(that.courseId)) return false;
    if (!userId.equals(that.userId)) return false;
    if (!course.equals(that.course)) return false;
    if (!user.equals(that.user)) return false;
    return grade != null ? grade.equals(that.grade) : that.grade == null;
  }

  @Override
  public int hashCode() {
    int result = course.hashCode();
    result = 31 * courseId.hashCode();
    result = 31 * result + userId.hashCode();
    result = 31 * result + user.hashCode();
    result = 31 * result + (grade != null ? grade.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StudentCourse{" +
      "courseId=" + courseId +
      ", userId=" + userId +
      ", course=" + course +
      ", user=" + user +
      ", grade='" + grade + '\'' +
      '}';
  }
}
