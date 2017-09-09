package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.AccountDTO;
import org.springframework.stereotype.Service;

@Service
public class AccountTransformer {

	public AccountTransformer() {
	}

	public AccountDTO transform(final User user) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(user.getId());
		accountDTO.setAuthority(user.getAuthority().getName());
		accountDTO.setLogin(user.getLogin());
		accountDTO.setFirstName(user.getFirstName());
		accountDTO.setLastName(user.getLastName());
		accountDTO.setEmail(user.getEmail());
		accountDTO.setPhoneNumber(user.getPhoneNumber());
		accountDTO.setStreetAddress1(user.getAddress().getStreetAddress1());
		accountDTO.setStreetAddress2(user.getAddress().getStreetAddress2());
		accountDTO.setCity(user.getAddress().getCity());
		accountDTO.setState(user.getAddress().getState().name());
		accountDTO.setPostalCode(user.getAddress().getPostalCode());
		accountDTO.setNotes(user.getNotes());
		accountDTO.setOrgRole(user.getOrgRole());
		return accountDTO;
	}

	// Never updates the id, authority, or login, needs to be done via other resources
	public User transform(final AccountDTO accountDTO) {
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
