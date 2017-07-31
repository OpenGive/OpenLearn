package org.openlearn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.openlearn.config.Constants;
import org.openlearn.domain.Course;
import org.openlearn.domain.Organization;
import org.openlearn.domain.StudentCourse;
import org.openlearn.domain.User;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.MailService;
import org.openlearn.service.StudentCourseService;
import org.openlearn.service.UserService;
import org.openlearn.service.dto.UserDTO;
import org.openlearn.web.rest.util.HeaderUtil;
import org.openlearn.web.rest.util.PaginationUtil;
import org.openlearn.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

	private final Logger log = LoggerFactory.getLogger(UserResource.class);

	private static final String ENTITY_NAME = "userManagement";

	private final UserRepository userRepository;

	private final MailService mailService;

	private final UserService userService;

	private final StudentCourseService studentCourseService;

	public UserResource(final UserRepository userRepository, final MailService mailService,
			final UserService userService, final StudentCourseService studentCourseService) {

		this.userRepository = userRepository;
		this.mailService = mailService;
		this.userService = userService;
		this.studentCourseService = studentCourseService;
	}

	/**
	 * POST  /users  : Creates a new user.
	 * <p>
	 * Creates a new user if the login and email are not already used, and sends an
	 * mail with an activation link.
	 * The user needs to be activated on creation.
	 * </p>
	 *
	 * @param managedUserVM the user to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
    @PostMapping("/users")
    @Timed
    @Secured({ AuthoritiesConstants.ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.ORG_ADMIN })
    public ResponseEntity<?> createUser(@RequestBody final ManagedUserVM managedUserVM,
        Authentication authentication) throws URISyntaxException
    {
        log.debug("REST request to save User : {}", managedUserVM);

        if(managedUserVM.getAuthorities() == null || managedUserVM.getAuthorities().size() == 0)
        {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "missingAuthority", "A new user must have at least one authority"))
                .body(null);
        }
        else if(!AuthoritiesConstants.hasHigherPermissions(authentication.getAuthorities().stream().map(
            GrantedAuthority::getAuthority).collect(Collectors.toList()), managedUserVM.getAuthorities()))
        {
            return new ResponseEntity<>("Insufficient permissions to create user with selected role", HttpStatus.UNAUTHORIZED);
        }
		else if (managedUserVM.getId() != null)
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
					.body(null);
		// Lowercase the user login before comparing with database
		else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent())
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
					.body(null);
		else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent())
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
					.body(null);
		else {
			final User newUser = userService.createUser(managedUserVM, managedUserVM.getPassword());

			mailService.sendCreationEmail(newUser);
			return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
					.headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
					.body(newUser);
		}
	}

	/**
	 * PUT  /users : Updates an existing User.
	 *
	 * @param managedUserVM the user to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated user,
	 * or with status 400 (Bad Request) if the login or email is already in use,
	 * or with status 500 (Internal Server Error) if the user couldn't be updated
	 */
	@PutMapping("/users")
	@Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.STUDENT})
	public ResponseEntity<UserDTO> updateUser(@RequestBody final ManagedUserVM managedUserVM) {
		log.debug("REST request to update User : {}", managedUserVM);
		Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
		if (existingUser.isPresent() && !existingUser.get().getId().equals(managedUserVM.getId()))
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
		existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
		if (existingUser.isPresent() && !existingUser.get().getId().equals(managedUserVM.getId()))
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
		final Optional<UserDTO> updatedUser = userService.updateUser(managedUserVM);

		return ResponseUtil.wrapOrNotFound(updatedUser,
				HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()));
	}

	/**
	 * GET  /users : get all users.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and with body all users
	 */
	@GetMapping("/users")
	@Timed
	public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam final Pageable pageable) {
		final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /users/:login : get the "login" user.
	 *
	 * @param login the login of the user to find
	 * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
	 */
	@GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	public ResponseEntity<UserDTO> getUser(@PathVariable final String login) {
		log.debug("REST request to get User : {}", login);
		return ResponseUtil.wrapOrNotFound(
				userService.getUserWithAuthoritiesByLogin(login)
				.map(UserDTO::new));
	}

	/**
	 * GET  /users/:login/courses : get the "login" user's courses.
	 *
	 * @param login the login of the user to find
	 * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
	 */
	@GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}/courses")
	@Timed
	public ResponseEntity<List<StudentCourse>> getUserCourses(@PathVariable final String login, Pageable pageable) {
		log.debug("REST request to get User : {}", login);
		final Page<StudentCourse> page = studentCourseService.findByLogin(login, pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/"+login+"/students");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}

	/**
	 * DELETE /users/:login : delete the "login" User.
	 *
	 * @param login the login of the user to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<Void> deleteUser(@PathVariable final String login) {
		log.debug("REST request to delete User: {}", login);
		userService.deleteUser(login);
		return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
	}

	/**
	 * DELETE /users/:login : delete the User by id.
	 *
	 * @param id the id of the user to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/users/{id:" + Constants.ID_REGEX + "}")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
		log.debug("REST request to delete User: {}", id);
		userService.deleteUser(id);
		return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", id.toString())).build();
	}

	@GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}/instructors")
	@Timed
	@Secured(AuthoritiesConstants.INSTRUCTOR)
	public ResponseEntity<List<Course>> getUserInstructors(@PathVariable final String login, Pageable pageable){
		log.debug("REST request to get courses instructed by user with login : {}", login);
		final Page<Course> page = userService.getCoursesInstructedByUser(login,pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users/"+login+"/instructors");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}/organizations")
	@Timed
	public ResponseEntity<Set<Organization>> getOrganizationsForUser(@PathVariable final String login){
		log.debug("REST request to get organizations for user : {}", login);
		final Set<Organization> orgs = userService.getOrganizationsForUser(login);
		return new ResponseEntity<Set<Organization>>(orgs, HttpStatus.OK);
	}

	@PostMapping("/users/{login:" + Constants.LOGIN_REGEX + "}/organizations")
	@Secured(AuthoritiesConstants.ADMIN)
	@Timed
	public ResponseEntity<User> addUserToOrganization(@PathVariable final String login, @RequestParam final Long organizationId){
		log.debug("REST request to add user {} to organization {}", login, organizationId);
		final User user = userService.addUserToOrganization(login, organizationId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}/organizations/{organizationId}")
	@Secured(AuthoritiesConstants.ADMIN)
	@Timed
	public ResponseEntity<User> removeUserFromOrganization(@PathVariable final String login, @PathVariable final Long organizationId){
		log.debug("REST request to remove user {} from organization {}", login, organizationId);
		final User user = userService.removeUserFromOrganization(login, organizationId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}