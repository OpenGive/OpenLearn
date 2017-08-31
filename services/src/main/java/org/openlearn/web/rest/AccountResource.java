package org.openlearn.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.openlearn.domain.User;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.MailService;
import org.openlearn.service.UserService;
import org.openlearn.service.dto.UserDTO;
import org.openlearn.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UserRepository userRepository;

	private final UserService userService;

	private final MailService mailService;

	public AccountResource(final UserRepository userRepository, final UserService userService,
	                       final MailService mailService) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.mailService = mailService;
	}

	/**
	 * GET  /account : get the current user.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
	 */
	@GetMapping("/account")
	@Timed
	public ResponseEntity<UserDTO> getAccount() {
		return Optional.ofNullable(userService.getUserWithAuthorities())
			.map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
			.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * POST  /account : update the current user information.
	 *
	 * @param userDTO the current user information
	 * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be updated
	 */
	@PostMapping("/account")
	@Timed
	public ResponseEntity<?> saveAccount(@Valid @RequestBody final UserDTO userDTO) {
		final Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
		if (existingUser.isPresent() && !existingUser.get().getLogin().equalsIgnoreCase(userDTO.getLogin()))
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
		return userRepository
			.findOneByLogin(SecurityUtils.getCurrentUserLogin())
			.map(u -> {
				userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
					userDTO.getPhoneNumber(), userDTO.getAddress(), userDTO.isFourteenPlus(), userDTO.getBiography());
				return new ResponseEntity<>(HttpStatus.OK);
			})
			.orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}
}
