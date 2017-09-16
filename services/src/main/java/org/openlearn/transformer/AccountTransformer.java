package org.openlearn.transformer;

import org.openlearn.domain.Address;
import org.openlearn.domain.User;
import org.openlearn.domain.enumeration.State;
import org.openlearn.dto.AccountDTO;
import org.openlearn.repository.AddressRepository;
import org.openlearn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
		if (!CollectionUtils.isEmpty(user.getAddresses())) {
			Address address = user.getAddresses().get(0);
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
	 * Transforms a DTO into an entity
	 * NOTE: Does not save the authority, or login, as that should be done using one of the other DTOs
	 *
	 * @param accountDTO DTO to transform
	 * @return the new entity
	 */
	public User transform(final AccountDTO accountDTO) {
		log.debug("Transforming account DTO to user : {}", accountDTO);
		User user = accountDTO.getId() == null ? new User() : userRepository.findOne(accountDTO.getId());
		// TODO: Error handling
		if (accountDTO.getFirstName() != null) user.setFirstName(accountDTO.getFirstName());
		if (accountDTO.getLastName() != null) user.setLastName(accountDTO.getLastName());
		if (accountDTO.getEmail() != null) user.setEmail(accountDTO.getEmail());
		if (accountDTO.getPhoneNumber() != null) user.setPhoneNumber(accountDTO.getPhoneNumber());
		if (!isAddressEmpty(accountDTO) || !CollectionUtils.isEmpty(user.getAddresses())) {
			Address address = (CollectionUtils.isEmpty(user.getAddresses()) ? new Address() : user.getAddresses().get(0));
			address.setStreetAddress1(accountDTO.getStreetAddress1());
			address.setStreetAddress2(accountDTO.getStreetAddress2());
			address.setCity(accountDTO.getCity());
			address.setState(State.valueOf(accountDTO.getState()));
			address.setPostalCode(accountDTO.getPostalCode());
			address.setUser(user);
			addressRepository.save(address);
		}
		if (accountDTO.getNotes() != null) user.setNotes(accountDTO.getNotes());
		if (accountDTO.getOrgRole() != null) user.setOrgRole(accountDTO.getOrgRole());
		return user;
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
