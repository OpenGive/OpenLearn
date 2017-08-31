package org.openlearn.service;

import java.util.Optional;

import org.openlearn.config.Constants;
import org.openlearn.domain.*;
import org.openlearn.repository.*;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;

	private final CourseRepository courseRepository;

	private final OrganizationRepository organizationRepository;

	private final PasswordEncoder passwordEncoder;

	public final JdbcTokenStore jdbcTokenStore;

	private final AuthorityRepository authorityRepository;

	private final AddressRepository addressRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JdbcTokenStore jdbcTokenStore,
	                   AuthorityRepository authorityRepository, AddressRepository addressRepository,
	                   CourseRepository courseRepository, OrganizationRepository organizationRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jdbcTokenStore = jdbcTokenStore;
		this.authorityRepository = authorityRepository;
		this.addressRepository = addressRepository;
		this.courseRepository = courseRepository;
		this.organizationRepository = organizationRepository;
	}

	public User createUser(String login, String password, String firstName, String lastName, String email,
	                       String phoneNumber, Address address, boolean fourteenPlus, String biography,
	                       Authority authority, Organization organization) {

		final User newUser = new User();
		final String encryptedPassword = passwordEncoder.encode(password);
		newUser.setLogin(login);
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setPhoneNumber(phoneNumber);
		newUser.setAddress(address);
		newUser.setFourteenPlus(fourteenPlus);
		newUser.setBiography(biography);
		newUser.setAuthority(authority);
		newUser.setOrganization(organization);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public User createUser(UserDTO userDTO, String password) {
		final User user = new User();

		user.setLogin(userDTO.getLogin());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		user.setPhoneNumber(userDTO.getPhoneNumber());
		user.setAddress(userDTO.getAddress());
		user.setFourteenPlus(userDTO.isFourteenPlus());
		user.setBiography(userDTO.getBiography());

		Authority authority = authorityRepository.findOne(userDTO.getAuthority());
		user.setAuthority(authority);

		Organization organization = organizationRepository.findOne(userDTO.getOrganizationId());
		user.setOrganization(organization);

		String encryptedPassword = passwordEncoder.encode(password);
		user.setPassword(encryptedPassword);

		userRepository.save(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	public void updateUser(final String firstName, final String lastName, final String email, final String phoneNumber,
	                       final Address address, final boolean fourteenPlus, final String biography) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setPhoneNumber(phoneNumber);
			user.setAddress(address);
			user.setFourteenPlus(fourteenPlus);
			user.setBiography(biography);
			log.debug("Changed Information for User: {}", user);
		});
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param userDTO user to update
	 * @return updated user
	 */
	public Optional<UserDTO> updateUser(final UserDTO userDTO, String password) {
		return Optional.of(userRepository
			.findOne(userDTO.getId()))
			.map(user -> {
				user.setLogin(userDTO.getLogin());
				user.setFirstName(userDTO.getFirstName());
				user.setLastName(userDTO.getLastName());
				user.setEmail(userDTO.getEmail());
				user.setPhoneNumber(userDTO.getPhoneNumber());
				user.setAddress(userDTO.getAddress());
				if (userDTO.getAddress() != null && userDTO.getAddress().getId() != null) {
					Address findAddress = addressRepository.findOne(userDTO.getAddress().getId());
					if (findAddress != null) {
						user.setAddress(findAddress);
					}
				}
				if (userDTO.getAddress() == null && user.getAddress() != null) {
					addressRepository.delete(user.getAddress().getId());
				}
				if (userDTO.getAddress() != null && userDTO.getAddress().isEmpty()) {
					addressRepository.delete(user.getAddress().getId());
				}
				user.setFourteenPlus(userDTO.isFourteenPlus());
				if (password != null) {
					String encryptedPassword = passwordEncoder.encode(password);
					user.setPassword(encryptedPassword);
				}
				user.setBiography(userDTO.getBiography());
				Organization organization = organizationRepository.findOne(userDTO.getOrganizationId());
				user.setOrganization(organization);
				log.debug("Changed Information for User: {}", user);
				return user;
			})
			.map(UserDTO::new);
	}

	public void deleteUser(final String login) {
		if (!validateAccess(login)) ;
		jdbcTokenStore.findTokensByUserName(login).forEach(jdbcTokenStore::removeAccessToken);
		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}

	public void deleteUser(final Long id) {
		User user = userRepository.findOne(id);
		deleteUser(user.getLogin());
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> getAllManagedUsers(final Pageable pageable) {
		if (!SecurityUtils.isAuthenticated()) {
			return null;
		}
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			log.debug("User has ADMIN authority");
			// get all users
			return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
		}
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT)) {
			log.debug("User has Student authority");
			// get users in org
			return userRepository.findOneByLogin(pageable, SecurityUtils.getCurrentUserLogin()).map(UserDTO::new);
		}
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.INSTRUCTOR)) {
			log.debug("User has Instructor authority");
			return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
		}

		log.debug("User does not have ADMIN or STUDENT authority 1");
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return userRepository.findAllByOrganizationId(pageable, user.get().getOrganization().getId()).map(UserDTO::new);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(final String login) {
		return userRepository.findOneWithAuthoritiesByLogin(login);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserByLogin(final String login) {
		return userRepository.findOneByLogin(login);
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities(final Long id) {
		return userRepository.findOneWithAuthoritiesById(id);
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
	}

	@Transactional(readOnly = true)
	public Page<Course> getCoursesInstructedByUser(final String login, final Pageable pageable) {
		Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(login);
		return courseRepository.findAllByInstructorId(user.get().getId(), pageable);
	}

	private boolean validateAccess(String login) {
		Optional<User> user = userRepository.findOneByLogin(login);
		return validateAccess(user.get());
	}

	private boolean validateAccess(Long id) {
		User user = userRepository.findOne(id);
		return validateAccess(user);
	}

	private boolean validateAccess(User user) {
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT)) {
			Optional<User> requestUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
			if (user.getId() != requestUser.get().getId()) {
				return false;
			}
		}

		if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
			(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORG_ADMIN) ||
				SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.INSTRUCTOR))) {
			Optional<User> currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
			Optional<User> changeUser = userRepository.findOneByLogin(user.getLogin());
			if (changeUser.get().getOrganization().getId() != currentUser.get().getOrganization().getId()) {
				return false;
			}
		}
		return true;
	}
}
