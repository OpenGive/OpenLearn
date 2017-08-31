package org.openlearn.repository;

import org.openlearn.domain.Address;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Address entity.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

}
