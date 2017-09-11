package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.AdminDTO;
import org.openlearn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminTransformer {

	private static final Logger log = LoggerFactory.getLogger(AdminTransformer.class);

	private final UserRepository userRepository;

	private final UserTransformer userTransformer;

	public AdminTransformer(final UserRepository userRepository, final UserTransformer userTransformer) {
		this.userRepository = userRepository;
		this.userTransformer = userTransformer;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param user entity to transform
	 * @return the new DTO
	 */
	public AdminDTO transform(final User user) {
		log.debug("Transforming user to admin DTO : {}", user);
		AdminDTO adminDTO = new AdminDTO();
		userTransformer.transformUserToDTO(adminDTO, user);
		return adminDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param adminDTO DTO to transform
	 * @return the new entity
	 */
	public User transform(final AdminDTO adminDTO) {
		log.debug("Transforming admin DTO to user : {}", adminDTO);
		User user = adminDTO.getId() == null ? new User() : userRepository.findOne(adminDTO.getId());
		// TODO: Error handling
		userTransformer.transformDTOToUser(user, adminDTO);
		return user;
	}
}
