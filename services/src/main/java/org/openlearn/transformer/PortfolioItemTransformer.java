package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.repository.PortfolioItemRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PortfolioItemTransformer {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private static final Logger log = LoggerFactory.getLogger(PortfolioItemTransformer.class);

	private final PortfolioItemRepository portfolioItemRepository;

	private final UserRepository userRepository;

	public PortfolioItemTransformer(final PortfolioItemRepository portfolioItemRepository,
	                                final UserRepository userRepository) {
		this.portfolioItemRepository = portfolioItemRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param portfolioItem entity to transform
	 * @return the new DTO
	 */
	public PortfolioItemDTO transform(final PortfolioItem portfolioItem) {
		log.debug("Transforming portfolio item to portfolio item DTO : {}", portfolioItem);
		PortfolioItemDTO portfolioItemDTO = new PortfolioItemDTO();
		portfolioItemDTO.setId(portfolioItem.getId());
		portfolioItemDTO.setName(portfolioItem.getName());
		portfolioItemDTO.setDescription(portfolioItem.getDescription());
		portfolioItemDTO.setStudentId(portfolioItem.getStudent().getId());
		portfolioItemDTO.setUrl(portfolioItem.getUrl());
		return portfolioItemDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param portfolioItemDTO DTO to transform
	 * @return the new entity
	 */
	public PortfolioItem transform(final PortfolioItemDTO portfolioItemDTO) {
		log.debug("Transforming portfolio item DTO to portfolio item : {}", portfolioItemDTO);
		PortfolioItem portfolioItem = portfolioItemDTO.getId() == null ? new PortfolioItem() : portfolioItemRepository.findOne(portfolioItemDTO.getId());
		// TODO: Error handling
		portfolioItem.setName(portfolioItemDTO.getName());
		portfolioItem.setDescription(portfolioItemDTO.getDescription());
		portfolioItem.setStudent(userRepository.findOneByIdAndAuthority(portfolioItemDTO.getStudentId(), STUDENT));
		if (portfolioItemDTO.getUrl() != null) portfolioItem.setUrl(portfolioItemDTO.getUrl());
		portfolioItem.setOrganization(portfolioItem.getStudent().getOrganization());
		return portfolioItem;
	}
}
