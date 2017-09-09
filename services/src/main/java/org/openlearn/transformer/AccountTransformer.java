package org.openlearn.transformer;

import org.openlearn.domain.User;
import org.openlearn.dto.AccountDTO;

public class AccountTransformer {

	public AccountTransformer() {
	}

	public AccountDTO transform(final User user) {
		AccountDTO accountDTO = new AccountDTO();
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
