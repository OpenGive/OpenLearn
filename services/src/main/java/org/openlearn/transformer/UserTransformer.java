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
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setLogin(userDTO.getLogin());
		user.setAuthority(authorityRepository.findOne(userDTO.getAuthority()));
		user.setEmail(userDTO.getEmail());
		user.setPhoneNumber(userDTO.getPhoneNumber());
		user.setNotes(userDTO.getNotes());
		if (!isAddressEmpty(userDTO) || !CollectionUtils.isEmpty(user.getAddresses())) {
			Address address = (CollectionUtils.isEmpty(user.getAddresses()) ? new Address() : user.getAddresses().get(0));
			address.setStreetAddress1(userDTO.getStreetAddress1());
			address.setStreetAddress2(userDTO.getStreetAddress2());
			address.setCity(userDTO.getCity());
			address.setState(State.valueOf(userDTO.getState()));
			address.setPostalCode(userDTO.getPostalCode());
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
