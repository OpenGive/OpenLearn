package org.openlearn.repository;

import org.openlearn.domain.User;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByActivationKey(String activationKey);

  List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

  Optional<User> findOneByResetKey(String resetKey);

  Optional<User> findOneByEmail(String email);

  Optional<User> findOneByLogin(String login);

  Page<User> findOneByLogin(Pageable pageable, String login);

  @EntityGraph(attributePaths = "authorities")
  User findOneWithAuthoritiesById(Long id);

  @EntityGraph(attributePaths = "authorities")
  Optional<User> findOneWithAuthoritiesByLogin(String login);

  Page<User> findAllByLoginNot(Pageable pageable, String login);

//  @EntityGraph(attributePaths = "authorities")
//  Page<User> findOneWithAuthoritiesByAuthorities(Pageable pageable, String authority);

//  @EntityGraph(attributePaths = "authorities")
//  Page<User> findAllWithAuthoritiesAndByOrganizationIdsAAndAuthoritiesIs(Pageable pageable, Long organizationId, String Authority);

  Set<User> findAllByOrganizationIds(Long organizationId);

  Page<User> findAllByOrganizationIdsIn(Pageable pageable,Set<Long> organizationIds);

	/**
<<<<<<< HEAD
	 * Finds all users of Role role
	 * @param role role to filter by
	 * @return Page<User> of role
	 */
	@Query(value = "Select * from user left join user_authority on user_authority.user_id = user.id where user_authority.AUTHORITY_NAME = ?1 ORDER BY /*#pageable*/;", nativeQuery = true)
	Page<User> findAllWithAuthoritiesByAuthorities(Pageable pageable, String role);

	/**
	 * Finds all users of Role role in org_id
	 * @param role role to filter by
	 * @param orgID ID of Org to filter by
	 * @return Page<User> of role
	 */
	@Query(value = "Select * from user  left join user_authority on user_authority.user_id = user.id "
		+" left join user_org on user_org.user_id = user.id "
		+"where user_authority.AUTHORITY_NAME = ?1 and user_org.org_id = ?2 ORDER BY /*#pageable*/;", nativeQuery = true)
	Page<User> findAllWithAuthoritiesAndByOrganizationIdsAndAuthoritiesIs(Pageable pageable, String role, Long orgID);

	/**
	 * Finds org_id of user
	 * @param login role to filter by
	 * @return Page<User> of role
	 */
	@Query(value = "Select * from user left join user_org uo on uo.user_id = user.id where user.login = ?1;", nativeQuery = true)
	Integer findOrgIDByLogin(String login);

	/**
	 * Finds all students not in course
	 * @param id course id to filter by
	 * @return List<User> of students
	 */
	@Query(value = "Select * from user left join user_authority ua on ua.user_id = user.id where ua.AUTHORITY_NAME = 'ROLE_STUDENT'  and user.id not in (select user_id from student_course where course_id= ?1);", nativeQuery = true)
	List<User> findAllByCourseIdNot(Long id);
}
