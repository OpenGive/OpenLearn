package org.openlearn.transformer;

import org.openlearn.domain.Address;
import org.openlearn.domain.User;
import org.openlearn.domain.enumeration.State;
import org.openlearn.dto.AccountDTO;
import org.openlearn.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountTransformer {

	private static final Logger log = LoggerFactory.getLogger(AccountTransformer.class);

	private final AddressRepository addressRepository;

	public AccountTransformer(final AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
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
		if (!user.getAddresses().isEmpty()) {
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
	 * NOTE: Does not save the id, authority, or login, as that should be done using one of the other DTOs
	 *
	 * @param accountDTO DTO to transform
	 * @return the new entity
	 */
	public User transform(final AccountDTO accountDTO) {
		log.debug("Transforming account DTO to user : {}", accountDTO);
		User user = new User();
		user.setFirstName(accountDTO.getFirstName());
		user.setLastName(accountDTO.getLastName());
		user.setEmail(accountDTO.getEmail());
		user.setPhoneNumber(accountDTO.getPhoneNumber());
		if (!isAddressEmpty(accountDTO)) {
			Address address = (user.getAddresses().isEmpty() ? new Address() : user.getAddresses().get(0));
			address.setStreetAddress1(accountDTO.getStreetAddress1());
			address.setStreetAddress2(accountDTO.getStreetAddress2());
			address.setCity(accountDTO.getCity());
			address.setState(State.valueOf(accountDTO.getState()));
			address.setPostalCode(accountDTO.getPostalCode());
			address.setUser(user);
			addressRepository.save(address);
		}
		user.setNotes(accountDTO.getNotes());
		user.setOrgRole(accountDTO.getOrgRole());
		return user;
	}

	private boolean isAddressEmpty(AccountDTO accountDTO) {
		return accountDTO.getStreetAddress1() == null
			&& accountDTO.getStreetAddress2() == null
			&& accountDTO.getCity() == null
			&& accountDTO.getState() == null
			&& accountDTO.getPostalCode() == null;
	}
}
