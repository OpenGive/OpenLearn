package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.UserDTO;
import org.openlearn.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

@Service
public class UserTransformer {

	private final AuthorityRepository authorityRepository;

	public UserTransformer(AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}

	public void transformUserToDTO(final UserDTO userDTO, final User user) {
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

	public void transformDTOToUser(final User user, final UserDTO userDTO) {
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
