package org.openlearn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.openlearn.dto.CourseDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.CourseService;
import org.openlearn.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/courses")
public class CourseResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/courses/";

	private static final Logger log = LoggerFactory.getLogger(CourseResource.class);

	private final CourseService courseService;

	private final StorageService storageService;

	public CourseResource(final CourseService courseService,
			final StorageService storageService) {
		this.courseService = courseService;
		this.storageService = storageService;
	}

	/**
	 * GET  /:id : get a single course by ID
	 *
	 * @param id the ID of the course to get
	 * @return the ResponseEntity with status 200 (OK) and the course in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity get(@PathVariable final Long id) {
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
	public ResponseEntity get(@ApiParam final Pageable pageable) {
		log.debug("GET request for all courses");
		Page<CourseDTO> response = courseService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
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
	public ResponseEntity create(@RequestBody @Valid final CourseDTO courseDTO) throws URISyntaxException {
		log.debug("POST request to create course : {}", courseDTO);
		CourseDTO response = courseService.save(courseDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * POST  / : upload a course file
	 *
	 * @param courseDTO the course to create
	 * @return the ResponseEntity with status 200 (OK) and the created course in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping(path="/upload")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public String uploadCourseFile(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws URISyntaxException {
		Long courseId = 1L;
        storageService.store(file, courseId);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "You successfully uploaded \" + file.getOriginalFilename() + \"!";
//		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
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
	public ResponseEntity update(@RequestBody @Valid final CourseDTO courseDTO) {
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
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete course : {}", id);
		courseService.delete(id);
		return ResponseEntity.ok().build();
	}
}
