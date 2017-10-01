package org.openlearn.transformer;

import org.openlearn.domain.Address;
import org.openlearn.domain.User;
import org.openlearn.domain.enumeration.State;
import org.openlearn.dto.AccountDTO;
import org.openlearn.repository.AddressRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.exception.OpenLearnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class AccountTransformer {

	private static final Logger log = LoggerFactory.getLogger(AccountTransformer.class);

	private final AddressRepository addressRepository;

	private final UserRepository userRepository;

	public AccountTransformer(final AddressRepository addressRepository, final UserRepository userRepository) {
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param user entity to transform
	 * @return the new DTO
	 */
	public AccountDTO transform(final User user) {
		log.debug("Transforming user to account DTO : {}", user);
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(user.getId());
		accountDTO.setAuthority(user.getAuthority().getName());
		accountDTO.setLogin(user.getLogin());
		accountDTO.setFirstName(user.getFirstName());
		accountDTO.setLastName(user.getLastName());
		accountDTO.setEmail(user.getEmail());
		accountDTO.setPhoneNumber(user.getPhoneNumber());
		if (user.getAddress() != null) {
			Address address = user.getAddress();
			accountDTO.setStreetAddress1(address.getStreetAddress1());
			accountDTO.setStreetAddress2(address.getStreetAddress2());
			accountDTO.setCity(address.getCity());
			accountDTO.setState(address.getState().name());
			accountDTO.setPostalCode(address.getPostalCode());
		}
		accountDTO.setNotes(user.getNotes());
		accountDTO.setOrgRole(user.getOrgRole());
		return accountDTO;
	}

	/**
	 * Transforms an account DTO into a user
	 *
	 * @param accountDTO DTO to transform from
	 * @param user user to merge to
	 * @return the user
	 * @throws OpenLearnException if there is a validation error
	 */
	public User transform(final AccountDTO accountDTO, final User user) throws OpenLearnException {
		switch(user.getAuthority().getName()) {
			case AuthoritiesConstants.ADMIN:
				return mergeAdmin(accountDTO, user);
			case AuthoritiesConstants.ORG_ADMIN:
				return mergeOrgAdmin(accountDTO, user);
			case AuthoritiesConstants.INSTRUCTOR:
				return mergeInstructor(accountDTO, user);
			case AuthoritiesConstants.STUDENT:
				return mergeStudent(accountDTO, user);
			default:
				// TODO: Error handling
				return null;
		}
	}

	private User mergeAdmin(final AccountDTO accountDTO, final User user) throws OpenLearnException {
		user.setFirstName(accountDTO.getFirstName());
		user.setLastName(accountDTO.getLastName());
		if (StringUtils.isEmpty(accountDTO.getEmail())) {
			throw new OpenLearnException("Email is required");
		} else {
			user.setEmail(accountDTO.getEmail());
		}
		user.setPhoneNumber(accountDTO.getPhoneNumber());
		mergeAddress(accountDTO, user);
		user.setNotes(accountDTO.getNotes());
		return user;
	}

	private User mergeOrgAdmin(final AccountDTO accountDTO, final User user) throws OpenLearnException {
		user.setFirstName(accountDTO.getFirstName());
		user.setLastName(accountDTO.getLastName());
		if (StringUtils.isEmpty(accountDTO.getEmail())) {
			throw new OpenLearnException("Email is required");
		} else {
			user.setEmail(accountDTO.getEmail());
		}
		user.setPhoneNumber(accountDTO.getPhoneNumber());
		mergeAddress(accountDTO, user);
		user.setNotes(accountDTO.getNotes());
		if (StringUtils.isEmpty(accountDTO.getOrgRole())) {
			throw new OpenLearnException("Org role is required");
		} else {
			user.setOrgRole(accountDTO.getOrgRole());
		}
		return user;
	}

	private User mergeInstructor(final AccountDTO accountDTO, final User user) throws OpenLearnException {
		user.setFirstName(accountDTO.getFirstName());
		user.setLastName(accountDTO.getLastName());
		if (StringUtils.isEmpty(accountDTO.getEmail())) {
			throw new OpenLearnException("Email is required");
		} else {
			user.setEmail(accountDTO.getEmail());
		}
		if (StringUtils.isEmpty(accountDTO.getPhoneNumber())) {
			throw new OpenLearnException("Phone number is required");
		} else {
			user.setPhoneNumber(accountDTO.getPhoneNumber());
		}
		mergeAddress(accountDTO, user);
		user.setNotes(accountDTO.getNotes());
		if (StringUtils.isEmpty(accountDTO.getOrgRole())) {
			throw new OpenLearnException("Org role is required");
		} else {
			user.setOrgRole(accountDTO.getOrgRole());
		}
		return user;
	}

	private User mergeStudent(final AccountDTO accountDTO, final User user) {
		user.setFirstName(accountDTO.getFirstName());
		user.setLastName(accountDTO.getLastName());
		user.setEmail(accountDTO.getEmail());
		user.setPhoneNumber(accountDTO.getPhoneNumber());
		mergeAddress(accountDTO, user);
		user.setNotes(accountDTO.getNotes());
		return user;
	}

	private void mergeAddress(final AccountDTO accountDTO, final User user) {
		if (!isAddressEmpty(accountDTO) || user.getAddress() != null) {
			Address address = (user.getAddress() == null) ? new Address() : user.getAddress();
			address.setStreetAddress1(accountDTO.getStreetAddress1());
			address.setStreetAddress2(accountDTO.getStreetAddress2());
			address.setCity(accountDTO.getCity());
			address.setState(State.valueOf(accountDTO.getState()));
			address.setPostalCode(accountDTO.getPostalCode());
			address.setUser(user);
			addressRepository.save(address);
		}
	}

	private boolean isAddressEmpty(AccountDTO accountDTO) {
		// TODO: Validate address
		return accountDTO.getStreetAddress1() == null
			&& accountDTO.getStreetAddress2() == null
			&& accountDTO.getCity() == null
			&& accountDTO.getState() == null
			&& accountDTO.getPostalCode() == null;
	}
}
