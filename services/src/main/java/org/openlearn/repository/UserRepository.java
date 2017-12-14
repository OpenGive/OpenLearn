package org.openlearn.repository;

import org.openlearn.domain.Authority;
import org.openlearn.domain.Organization;
import org.openlearn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	User findOneByLogin(String login);

	User findOneByIdAndAuthority(Long id, Authority authority);

	List<User> findByAuthority(Authority authority);

	List<User> findByOrganizationAndAuthority(Organization organization, Authority authority);

	@Query(nativeQuery = true,
		countQuery = "Select count(*) from user where authority = 'ROLE_STUDENT'  and user.id not in (select student_id from student_course where course_id= :courseId)",
		value = "Select * from user where authority = 'ROLE_STUDENT'  and user.id not in (select student_id from student_course where course_id= :courseId)")
	List<User> findStudentsNotInCourse(@Param("courseId") Long courseId);

	@Query(nativeQuery = true,
		countQuery = "Select count(*) from user where authority = 'ROLE_STUDENT' organization_id= :organizationId  and user.id not in (select student_id from student_course where course_id= :courseId)",
		value = "Select * from user where authority = 'ROLE_STUDENT'  and organization_id= :organizationId and user.id not in (select student_id from student_course where course_id= :courseId)")
	List<User> findStudentsNotInCourseByOrganization(@Param("courseId") Long courseId, @Param("organizationId") Long organizationId);
}
