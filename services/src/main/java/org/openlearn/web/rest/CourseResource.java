package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.CourseDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.CourseService;
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
@RequestMapping("/api/courses")
public class CourseResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/courses/";

	private final Logger log = LoggerFactory.getLogger(CourseResource.class);

	private final CourseService courseService;

	public CourseResource(CourseService courseService) {
		this.courseService = courseService;
	}

	/**
	 * GET  /:id : get a single course by ID
	 *
	 * @param id the ID of the course to get
	 * @return the ResponseEntity with status 200 (OK) and the course in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable Long id) {
		log.debug("GET request to get course : {}", id);
		CourseDTO response = courseService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all course, filtered by organization
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of courses in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam Pageable pageable) {
		log.debug("GET request for all courses");
		Page<CourseDTO> response = courseService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a course
	 *
	 * @param courseDTO the course to create
	 * @return the ResponseEntity with status 200 (OK) and the created course in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid CourseDTO courseDTO) throws URISyntaxException {
		log.debug("POST request to create course : {}", courseDTO);
		CourseDTO response = courseService.save(courseDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update a course
	 *
	 * @param courseDTO the course to update
	 * @return the ResponseEntity with status 200 (OK) and the updated course in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid CourseDTO courseDTO) {
		log.debug("PUT request to update course : {}", courseDTO);
		CourseDTO response = courseService.save(courseDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a course
	 *
	 * @param id the ID of the course to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable Long id) {
		log.debug("DELETE request to delete course : {}", id);
		courseService.delete(id);
		return ResponseEntity.ok().build();
	}
}
