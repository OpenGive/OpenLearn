package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.StudentDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/students")
public class StudentResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/students/";

	private final Logger log = LoggerFactory.getLogger(StudentResource.class);

	private final StudentService studentService;

	public StudentResource(StudentService studentService) {
		this.studentService = studentService;
	}

	/**
	 * GET  /:id : get a single student user by ID
	 *
	 * @param id the ID of the student to get
	 * @return the ResponseEntity with status 200 (OK) and the student in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable Long id) {
		log.debug("GET request to get student : {}", id);
		StudentDTO response = studentService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all student users, filtered by organization
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of students in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam Pageable pageable) {
		log.debug("GET request for all students");
		Page<StudentDTO> response = studentService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
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
	public ResponseEntity create(@RequestBody @Valid StudentDTO studentDTO) throws URISyntaxException {
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
	public ResponseEntity update(@RequestBody @Valid StudentDTO studentDTO) {
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
	@DeleteMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable Long id) {
		log.debug("DELETE request to delete student : {}", id);
		studentService.delete(id);
		return ResponseEntity.ok().build();
	}
}
