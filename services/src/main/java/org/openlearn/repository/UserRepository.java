package org.openlearn.repository;

import org.openlearn.domain.Authority;
import org.openlearn.domain.Organization;
import org.openlearn.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	User findOneByLogin(String login);

	User findOneByIdAndAuthority(Long id, Authority authority);

	Page<User> findByAuthority(Authority authority, Pageable pageable);

	Page<User> findByOrganizationAndAuthority(Organization organization, Authority authority, Pageable pageable);
}
