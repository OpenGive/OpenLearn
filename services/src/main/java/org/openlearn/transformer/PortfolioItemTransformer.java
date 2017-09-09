package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.springframework.stereotype.Service;

@Service
public class PortfolioItemTransformer {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private final UserRepository userRepository;

	public PortfolioItemTransformer(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public PortfolioItemDTO transform(final PortfolioItem portfolioItem) {
		PortfolioItemDTO portfolioItemDTO = new PortfolioItemDTO();
		portfolioItemDTO.setId(portfolioItem.getId());
		portfolioItemDTO.setName(portfolioItem.getName());
		portfolioItemDTO.setDescription(portfolioItem.getDescription());
		portfolioItemDTO.setStudentId(portfolioItem.getStudent().getId());
		portfolioItemDTO.setUrl(portfolioItem.getUrl());
		return portfolioItemDTO;
	}

	public PortfolioItem transform(final PortfolioItemDTO portfolioItemDTO) {
		PortfolioItem portfolioItem = new PortfolioItem();
		portfolioItem.setId(portfolioItemDTO.getId());
		portfolioItem.setName(portfolioItemDTO.getName());
		portfolioItem.setDescription(portfolioItemDTO.getDescription());
		portfolioItem.setStudent(userRepository.findOneByIdAndAuthority(portfolioItemDTO.getStudentId(), STUDENT));
		portfolioItem.setUrl(portfolioItemDTO.getUrl());
		return portfolioItem;
	}
}
