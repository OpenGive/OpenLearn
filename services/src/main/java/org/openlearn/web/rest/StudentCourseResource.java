package org.openlearn.web.rest;

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
import java.util.List;

@RestController
@RequestMapping("/api/student-courses")
public class StudentCourseResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/student-courses/";

	private static final Logger log = LoggerFactory.getLogger(StudentCourseResource.class);

	private final StudentCourseService studentCourseService;

	public StudentCourseResource(final StudentCourseService studentCourseService) {
		this.studentCourseService = studentCourseService;
	}

	/**
	 * GET  /:id : get a single studentCourse by ID
	 *
	 * @param id the ID of the studentCourse to get
	 * @return the ResponseEntity with status 200 (OK) and the studentCourse in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get studentCourse : {}", id);
		StudentCourseDTO response = studentCourseService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  /student/:id : get a list of studentCourse by student
	 *
	 * @param id the ID of the student
	 * @return the ResponseEntity with status 200 (OK) and a list of studentCourse with course objects in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/student/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getByStudent(@PathVariable final Long id) {
		log.debug("GET request to get studentCourses by student : {}", id);
		List<StudentCourseDTO> response = studentCourseService.findByStudent(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  /course/:id : get a list of studentCourse by course
	 *
	 * @param id the ID of the course
	 * @return the ResponseEntity with status 200 (OK) and a list of studentCourse with student objects in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/course/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity getByCourse(@PathVariable final Long id) {
		log.debug("GET request to get studentCourses by course : {}", id);
		List<StudentCourseDTO> response = studentCourseService.findByCourse(id);
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
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid final StudentCourseDTO studentCourseDTO) throws URISyntaxException {
		log.debug("POST request to create studentCourse : {}", studentCourseDTO);
		StudentCourseDTO response = studentCourseService.create(studentCourseDTO);
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
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid final StudentCourseDTO studentCourseDTO) {
		log.debug("PUT request to update studentCourse : {}", studentCourseDTO);
		StudentCourseDTO response = studentCourseService.update(studentCourseDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a studentCourse
	 *
	 * @param id the ID of the studentCourse to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete studentCourse : {}", id);
		studentCourseService.delete(id);
		return ResponseEntity.ok().build();
	}
}
