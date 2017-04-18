package org.opengive.denver.stem.repository;

import org.opengive.denver.stem.domain.ItemLink;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemLink entity.
 */
@SuppressWarnings("unused")
public interface ItemLinkRepository extends JpaRepository<ItemLink,Long> {

}
