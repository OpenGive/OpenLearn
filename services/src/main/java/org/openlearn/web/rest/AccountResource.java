package org.openlearn.web.rest;

import javax.validation.Valid;

import org.openlearn.dto.AccountDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.UserService;
import org.openlearn.service.exception.OpenLearnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the current user's account information.
 */
@RestController
@RequestMapping("/api/account")
public class AccountResource {

	private static final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UserService userService;

	public AccountResource(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * GET  / : get the current user
	 *
	 * @return the ResponseEntity with status 200 (OK) and the current user in body
	 *      or with ... // TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity get() {
		log.debug("GET request to get current user account info");
		AccountDTO response = userService.getCurrentUserAccount();
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : update the current user information
	 *
	 * @param accountDTO the current user information
	 * @return the ResponseEntity with status 200 (OK) and the current user in body
	 *      or with ... // TODO: Error handling
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity update(@RequestBody @Valid final AccountDTO accountDTO) {
		log.debug("POST request to update current user account info : {}", accountDTO);
		try {
			AccountDTO response = userService.updateCurrentUserAccount(accountDTO);
			return ResponseEntity.ok(response);
		} catch (OpenLearnException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
