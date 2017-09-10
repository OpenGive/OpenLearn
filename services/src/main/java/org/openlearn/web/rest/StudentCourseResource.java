package org.openlearn.web.rest;

import org.openlearn.domain.StudentCourse;
import org.openlearn.dto.StudentCourseDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.StudentCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/student-courses")
public class StudentCourseResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/student-courses/";

	private final Logger log = LoggerFactory.getLogger(StudentCourseResource.class);

	private final StudentCourseService studentCourseService;

	public StudentCourseResource(StudentCourseService studentCourseService) {
		this.studentCourseService = studentCourseService;
	}

	/**
	 * GET  /:id : get a single studentCourse by ID
	 *
	 * @param id the ID of the studentCourse to get
	 * @return the ResponseEntity with status 200 (OK) and the studentCourse in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable Long id) {
		log.debug("GET request to get studentCourse : {}", id);
		StudentCourseDTO response = studentCourseService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a studentCourse
	 *
	 * @param studentCourseDTO the studentCourse to create
	 * @return the ResponseEntity with status 200 (OK) and the created studentCourse in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity create(@RequestBody @Valid StudentCourseDTO studentCourseDTO) throws URISyntaxException {
		log.debug("POST request to create studentCourse : {}", studentCourseDTO);
		StudentCourseDTO response = studentCourseService.save(studentCourseDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update a studentCourse
	 *
	 * @param studentCourseDTO the studentCourse to update
	 * @return the ResponseEntity with status 200 (OK) and the updated studentCourse in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity update(@RequestBody @Valid StudentCourseDTO studentCourseDTO) {
		log.debug("PUT request to update studentCourse : {}", studentCourseDTO);
		StudentCourseDTO response = studentCourseService.save(studentCourseDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a studentCourse
	 *
	 * @param id the ID of the studentCourse to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity delete(@PathVariable Long id) {
		log.debug("DELETE request to delete studentCourse : {}", id);
		studentCourseService.delete(id);
		return ResponseEntity.ok().build();
	}
}
