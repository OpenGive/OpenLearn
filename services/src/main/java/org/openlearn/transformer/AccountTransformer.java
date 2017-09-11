package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.AccountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountTransformer {

	private static final Logger log = LoggerFactory.getLogger(AccountTransformer.class);

	public AccountTransformer() {
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
			accountDTO.setStreetAddress1(user.getAddress().getStreetAddress1());
			accountDTO.setStreetAddress2(user.getAddress().getStreetAddress2());
			accountDTO.setCity(user.getAddress().getCity());
			accountDTO.setState(user.getAddress().getState().name());
			accountDTO.setPostalCode(user.getAddress().getPostalCode());
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
		// TODO: Save address
		user.setNotes(accountDTO.getNotes());
		user.setOrgRole(accountDTO.getOrgRole());
		return user;
	}
}
