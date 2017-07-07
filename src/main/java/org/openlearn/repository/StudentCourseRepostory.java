package org.openlearn.repository;

import org.openlearn.domain.CourseStudent;
import org.openlearn.domain.StudentCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by btaylor on 7/7/17.
 */
public interface StudentCourseRepostory extends JpaRepository<StudentCourse, Long> {
	Page<StudentCourse> findAllByUserId(Long userId, Pageable pageable);
}
