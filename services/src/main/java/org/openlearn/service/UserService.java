package org.openlearn.service;

import org.openlearn.domain.User;
import org.openlearn.dto.AccountDTO;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.AccountTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing User.
 */
@Service
@Transactional
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final AccountTransformer accountTransformer;

	private final UserRepository userRepository;

	public UserService(final AccountTransformer accountTransformer, final UserRepository userRepository) {
		this.accountTransformer = accountTransformer;
		this.userRepository = userRepository;
	}

	/**
	 * Get the current logged in user
	 *
	 * @return the current logged in user object
	 */
	@Transactional(readOnly = true)
	public User getCurrentUser() {
		log.debug("Request to get current user");
		return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
	}

	/**
	 * Get the current logged in user account info
	 *
	 * @return current user account info
	 */
	@Transactional(readOnly = true)
	public AccountDTO getCurrentUserAccount() {
		log.debug("Request to get current user account information");
		return accountTransformer.transform(getCurrentUser());
	}

	/**
	 * Save the current logged in user account info
	 *
	 * @param accountDTO account info to update
	 * @return updated current user account info
	 */
	public AccountDTO updateCurrentUserAccount(final AccountDTO accountDTO) {
		log.debug("Request to update current user account information");
		User user = mergeAccountInfo(accountDTO, getCurrentUser());
		return accountTransformer.transform(userRepository.save(user));
	}

	private User mergeAccountInfo(final AccountDTO accountDTO, final User user) {
		// TODO: Validate based on role (authority)
		if (!accountDTO.getFirstName().equals(user.getFirstName())) {
			user.setFirstName(accountDTO.getFirstName());
		}
		if (!accountDTO.getLastName().equals(user.getLastName())) {
			user.setLastName(accountDTO.getLastName());
		}
		if (!accountDTO.getEmail().equals(user.getEmail())) {
			user.setEmail(accountDTO.getEmail());
		}
		if (!accountDTO.getPhoneNumber().equals(user.getPhoneNumber())) {
			user.setPhoneNumber(accountDTO.getPhoneNumber());
		}
		// TODO: Save address
		if (!accountDTO.getNotes().equals(user.getNotes())) {
			user.setNotes(accountDTO.getNotes());
		}
		if (!accountDTO.getOrgRole().equals(user.getOrgRole())) {
			user.setOrgRole(accountDTO.getOrgRole());
		}
		return user;
	}
}
