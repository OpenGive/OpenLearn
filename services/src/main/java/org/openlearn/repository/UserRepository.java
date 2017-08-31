package org.openlearn.repository;

import org.openlearn.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByEmail(String email);

	Optional<User> findOneByLogin(String login);

	Page<User> findOneByLogin(Pageable pageable, String login);

	@EntityGraph(attributePaths = "authorities")
	User findOneWithAuthoritiesById(Long id);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByLogin(String login);

	Page<User> findAllByLoginNot(Pageable pageable, String login);

	Page<User> findAllByOrganizationId(Pageable pageable, Long organizationId);

	/**
	 * Finds all students not in course
	 *
	 * @param id course id to filter by
	 * @return List<User> of students
	 */
	@Query(value = "Select * from user left join user_authority ua on ua.user_id = user.id where ua.AUTHORITY_NAME = 'ROLE_STUDENT'  and user.id not in (select user_id from student_course where course_id= ?1);", nativeQuery = true)
	List<User> findAllByCourseIdNot(Long id);
}
