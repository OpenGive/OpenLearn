package org.openlearn.repository;

import org.openlearn.domain.ItemLink;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ItemLink entity.
 */
@SuppressWarnings("unused")
public interface ItemLinkRepository extends JpaRepository<ItemLink,Long> {

}
