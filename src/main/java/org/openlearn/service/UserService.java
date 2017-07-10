package org.openlearn.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.openlearn.config.Constants;
import org.openlearn.domain.Address;
import org.openlearn.domain.Authority;
import org.openlearn.domain.Course;
import org.openlearn.domain.User;
import org.openlearn.repository.AddressRepository;
import org.openlearn.repository.AuthorityRepository;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.repository.search.UserSearchRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.dto.UserDTO;
import org.openlearn.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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

  private final PasswordEncoder passwordEncoder;

  private final SocialService socialService;

  public final JdbcTokenStore jdbcTokenStore;

  private final UserSearchRepository userSearchRepository;

  private final AuthorityRepository authorityRepository;

  private final AddressRepository addressRepository;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SocialService socialService, JdbcTokenStore jdbcTokenStore, UserSearchRepository userSearchRepository, AuthorityRepository authorityRepository, AddressRepository addressRepository, CourseRepository courseRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.socialService = socialService;
    this.jdbcTokenStore = jdbcTokenStore;
    this.userSearchRepository = userSearchRepository;
    this.authorityRepository = authorityRepository;
    this.addressRepository = addressRepository;
    this.courseRepository = courseRepository;
  }

  public Optional<User> activateRegistration(String key) {
    log.debug("Activating user for activation key {}", key);
    return userRepository.findOneByActivationKey(key)
      .map(user -> {
        // activate given user for the registration key.
        user.setActivated(true);
        user.setActivationKey(null);
        userSearchRepository.save(user);
        log.debug("Activated user: {}", user);
        return user;
      });
  }

  public Optional<User> completePasswordReset(String newPassword, String key) {
    log.debug("Reset user password for reset key {}", key);

    return userRepository.findOneByResetKey(key)
      .filter(user -> {
        ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
        return user.getResetDate().isAfter(oneDayAgo);
      })
      .map(user -> {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetKey(null);
        user.setResetDate(null);
        return user;
      });
  }

  public Optional<User> requestPasswordReset(String login) {
    return userRepository.findOneByLogin(login)
      .filter(User::getActivated)
      .map(user -> {
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        return user;
      });
  }

  public User createUser(String login, String password, String firstName, String lastName, String email,
                         String phoneNumber, Address address, String imageUrl) {

    final User newUser = new User();
    final Authority authority = authorityRepository.findOne(AuthoritiesConstants.STUDENT);
    final Set<Authority> authorities = new HashSet<>();
    final String encryptedPassword = passwordEncoder.encode(password);
    newUser.setLogin(login);
    // new user gets initially a generated password
    newUser.setPassword(encryptedPassword);
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setEmail(email);
    newUser.setPhoneNumber(phoneNumber);
    newUser.setAddress(address);
    newUser.setImageUrl(imageUrl);
    // new user is not active
    newUser.setActivated(false);
    // new user gets registration key
    newUser.setActivationKey(RandomUtil.generateActivationKey());
    authorities.add(authority);
    newUser.setAuthorities(authorities);
    userRepository.save(newUser);
    userSearchRepository.save(newUser);
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
    user.setImageUrl(userDTO.getImageUrl());
    user.setBiography(userDTO.getBiography());
    user.setIs14Plus(userDTO.getIs14Plus());
    if (userDTO.getAuthorities() != null) {

      Set<Authority> authorities = new HashSet<>();
      userDTO.getAuthorities().forEach(
        authority -> authorities.add(authorityRepository.findOne(authority))
      );
      user.setAuthorities(authorities);
    } else {
      Set<Authority> authorities = new HashSet<>();
      authorities.add(authorityRepository.findOne(AuthoritiesConstants.STUDENT));
      user.setAuthorities(authorities);
    }

    String encryptedPassword = passwordEncoder.encode(password);
    user.setPassword(encryptedPassword);
    user.setResetKey(RandomUtil.generateResetKey());
    user.setResetDate(ZonedDateTime.now());
    user.setActivated(true);
    userRepository.save(user);
    userSearchRepository.save(user);
    log.debug("Created Information for User: {}", user);
    return user;
  }

  /**
   * Update basic information (first name, last name, email, language) for the current user.
   *
   * @param firstName first name of user
   * @param lastName  last name of user
   * @param email     email id of user
   * @param imageUrl  image URL of user
   */
  public void updateUser(final String firstName, final String lastName, final String email, final String phoneNumber, final Address address, final String imageUrl, final String biography) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setEmail(email);
      user.setPhoneNumber(phoneNumber);
      user.setAddress(address);
      user.setImageUrl(imageUrl);
      user.setBiography(biography);
      userSearchRepository.save(user);
      log.debug("Changed Information for User: {}", user);
    });
  }

  /**
   * Update all information for a specific user, and return the modified user.
   *
   * @param userDTO user to update
   * @return updated user
   */
  public Optional<UserDTO> updateUser(final UserDTO userDTO) {
    return Optional.of(userRepository
      .findOne(userDTO.getId()))
      .map(user -> {
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        if(userDTO.getAddress() == null || (userDTO.getAddress() != null && userDTO.getAddress().isEmpty())){
        	if(user.getAddress() != null){
				addressRepository.delete(user.getAddress());
			}
			user.setAddress(null);
		  }else{
			  user.setAddress(userDTO.getAddress());
		  }
        user.setImageUrl(userDTO.getImageUrl());
        user.setActivated(userDTO.isActivated());
        user.setBiography(userDTO.getBiography());
        user.setIs14Plus(userDTO.is14Plus());
        final Set<Authority> managedAuthorities = user.getAuthorities();
        managedAuthorities.clear();
        userDTO.getAuthorities().stream()
          .map(authorityRepository::findOne)
          .forEach(managedAuthorities::add);
        log.debug("Changed Information for User: {}", user);
        return user;
      })
      .map(UserDTO::new);
  }

  public void deleteUser(final String login) {
    jdbcTokenStore.findTokensByUserName(login).forEach(token ->
      jdbcTokenStore.removeAccessToken(token));
    userRepository.findOneByLogin(login).ifPresent(user -> {
      socialService.deleteUserSocialConnection(user.getLogin());
      userRepository.delete(user);
      userSearchRepository.delete(user);
      log.debug("Deleted User: {}", user);
    });
  }

  public void changePassword(final String password) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
      final String encryptedPassword = passwordEncoder.encode(password);
      user.setPassword(encryptedPassword);
      log.debug("Changed password for User: {}", user);
    });
  }

  @Transactional(readOnly = true)
  public Page<UserDTO> getAllManagedUsers(final Pageable pageable) {
    return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
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

  /**
   * Not activated users should be automatically deleted after 3 days.
   * <p>
   * This is scheduled to get fired everyday, at 01:00 (am).
   * </p>
   */
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    final ZonedDateTime now = ZonedDateTime.now();
    final List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
    for (final User user : users) {
      log.debug("Deleting not activated user {}", user.getLogin());
      userRepository.delete(user);
      userSearchRepository.delete(user);
    }
  }

  @Transactional(readOnly = true)
	public Page<Course> getCoursesInstructedByUser(final String login, final Pageable pageable){
  	Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(login);
  	Page<Course> result = courseRepository.findAllByInstructorId(user.get().getId(), pageable);
  	return result;
  }
}
