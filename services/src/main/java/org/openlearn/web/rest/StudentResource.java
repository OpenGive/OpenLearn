package org.openlearn.web.rest;

import org.openlearn.dto.StudentDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.StudentService;
import org.openlearn.service.UserService;
import org.openlearn.web.rest.errors.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/students/";

	private static final Logger log = LoggerFactory.getLogger(StudentResource.class);

	private final StudentService studentService;

	private final UserService userService;

	public StudentResource(final StudentService studentService,
						   final UserService userService) {
		this.studentService = studentService;
		this.userService = userService;
	}

	/**
	 * GET  /:id : get a single student user by ID
	 *
	 * @param id the ID of the student to get
	 * @return the ResponseEntity with status 200 (OK) and the student in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get student : {}", id);

		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT) &&
			!userService.getCurrentUser().getId().equals(id)) {

			log.error("Access denied for GET request to get student: {}", id);
			throw new AccessDeniedException();
		}

		StudentDTO response = studentService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all student users, filtered by organization
	 *
	 * @return the ResponseEntity with status 200 (OK) and a list of students in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get() {
		log.debug("GET request for all students");
		List<StudentDTO> response = studentService.findAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "/notInCourse/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity getStudentNotInCourse(@PathVariable final Long id) {
		log.debug("GET request for students not in course: " + id.toString());
		List<StudentDTO> response = studentService.findStudentsNotInCourse(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a student user
	 *
	 * @param studentDTO the student to create
	 * @return the ResponseEntity with status 200 (OK) and the created student in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid final StudentDTO studentDTO) throws URISyntaxException {
		log.debug("POST request to create student : {}", studentDTO);
		StudentDTO response = studentService.save(studentDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update a student user
	 *
	 * @param studentDTO the student to update
	 * @return the ResponseEntity with status 200 (OK) and the updated student in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid final StudentDTO studentDTO) {
		log.debug("PUT request to update student : {}", studentDTO);
		StudentDTO response = studentService.save(studentDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a student user
	 *
	 * @param id the ID of the student to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete student : {}", id);
		studentService.delete(id);
		return ResponseEntity.ok().build();
	}
}
