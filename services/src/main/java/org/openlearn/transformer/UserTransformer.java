package org.openlearn.transformer;

import org.openlearn.domain.Address;
import org.openlearn.domain.User;
import org.openlearn.domain.enumeration.State;
import org.openlearn.dto.UserDTO;
import org.openlearn.repository.AddressRepository;
import org.openlearn.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UserTransformer {

	private static final Logger log = LoggerFactory.getLogger(UserTransformer.class);

	private final AddressRepository addressRepository;

	private final AuthorityRepository authorityRepository;

	public UserTransformer(final AddressRepository addressRepository, final AuthorityRepository authorityRepository) {
		this.addressRepository = addressRepository;
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
		if (!CollectionUtils.isEmpty(user.getAddresses())) {
			Address address = user.getAddresses().get(0);
			userDTO.setStreetAddress1(address.getStreetAddress1());
			userDTO.setStreetAddress2(address.getStreetAddress2());
			userDTO.setCity(address.getCity());
			userDTO.setState(address.getState().name());
			userDTO.setPostalCode(address.getPostalCode());
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
		if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
		if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
		if (userDTO.getLogin() != null) user.setLogin(userDTO.getLogin());
		if (userDTO.getAuthority() != null) user.setAuthority(authorityRepository.findOne(userDTO.getAuthority()));
		if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
		if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
		if (userDTO.getNotes() != null) user.setNotes(userDTO.getNotes());
		if (!isAddressEmpty(userDTO)) {
			Address address = (CollectionUtils.isEmpty(user.getAddresses()) ? new Address() : user.getAddresses().get(0));
			if (userDTO.getStreetAddress1() != null) address.setStreetAddress1(userDTO.getStreetAddress1());
			if (userDTO.getStreetAddress2() != null) address.setStreetAddress2(userDTO.getStreetAddress2());
			if (userDTO.getCity() != null) address.setCity(userDTO.getCity());
			if (userDTO.getState() != null) address.setState(State.valueOf(userDTO.getState()));
			if (userDTO.getPostalCode() != null) address.setPostalCode(userDTO.getPostalCode());
			address.setUser(user);
			addressRepository.save(address);
		}
	}

	private boolean isAddressEmpty(UserDTO userDTO) {
		// TODO: Validate address
		return userDTO.getStreetAddress1() == null
			&& userDTO.getStreetAddress2() == null
			&& userDTO.getCity() == null
			&& userDTO.getState() == null
			&& userDTO.getPostalCode() == null;
	}
}
