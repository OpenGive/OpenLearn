package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.UserDTO;
import org.openlearn.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserTransformer {

	private static final Logger log = LoggerFactory.getLogger(UserTransformer.class);

	private final AuthorityRepository authorityRepository;

	public UserTransformer(final AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}

	/**
	 * Transforms a user to a user DTO
	 *
	 * @param userDTO DTO to transform to
	 * @param user user to transform from
	 */
	public void transformUserToDTO(final UserDTO userDTO, final User user) {
		log.debug("Transforming user to user DTO : {}", user);
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setLogin(user.getLogin());
		userDTO.setAuthority(user.getAuthority().getName());
		userDTO.setEmail(user.getEmail());
		userDTO.setPhoneNumber(user.getPhoneNumber());
		userDTO.setNotes(user.getNotes());
		if (user.getAddress() != null) {
			userDTO.setStreetAddress1(user.getAddress().getStreetAddress1());
			userDTO.setStreetAddress2(user.getAddress().getStreetAddress2());
			userDTO.setCity(user.getAddress().getCity());
			userDTO.setState(user.getAddress().getState().name());
			userDTO.setPostalCode(user.getAddress().getPostalCode());
		}
	}

	/**
	 * Transforms a user DTO to a user
	 *
	 * @param user user to transform to
	 * @param userDTO DTO to transform from
	 */
	public void transformDTOToUser(final User user, final UserDTO userDTO) {
		log.debug("Transforming user DTO to user : {}", userDTO);
		user.setId(userDTO.getId());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setLogin(userDTO.getLogin());
		user.setAuthority(authorityRepository.findOne(userDTO.getAuthority()));
		user.setEmail(userDTO.getEmail());
		user.setPhoneNumber(userDTO.getPhoneNumber());
		user.setNotes(userDTO.getNotes());
		// TODO: Save Address
	}
}
