package org.openlearn.domain;

import java.io.Serializable;

public class StudentCourseId implements Serializable{
  private Long courseId;
  private Long userId;

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StudentCourseId)) return false;

    StudentCourseId that = (StudentCourseId) o;

    if (!courseId.equals(that.courseId)) return false;
    return userId.equals(that.userId);
  }

  @Override
  public int hashCode() {
    int result = courseId.hashCode();
    result = 31 * result + userId.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "StudentCourseId{" +
      "courseId=" + courseId +
      ", userId=" + userId +
      '}';
  }
}
