package org.openlearn.web.rest;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.AssignmentService;
import org.openlearn.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/assignments/";

	private static final Logger log = LoggerFactory.getLogger(AssignmentResource.class);

	private final AssignmentService assignmentService;

	private final StorageService storageService;

	public AssignmentResource(final AssignmentService assignmentService, final StorageService storageService) {
		this.assignmentService = assignmentService;
		this.storageService = storageService;
	}

	/**
	 * GET  /:id : get a single assignment by ID
	 *
	 * @param id the ID of the assignment to get
	 * @return the ResponseEntity with status 200 (OK) and the assignment in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get assignment : {}", id);
		AssignmentDTO response = assignmentService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all assignment, filtered by organization
	 *
	 * @return the ResponseEntity with status 200 (OK) and a list of assignments in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get() {
		log.debug("GET request for all assignments");
		List<AssignmentDTO> response = assignmentService.findAll();
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all assignments for a course
	 *
	 * @return the ResponseEntity with status 200 (OK) and a list of assignments in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/course/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity getByCourse(@PathVariable final Long id) {
		log.debug("GET request for all assignments by course: {}", id);
		List<AssignmentDTO> response = assignmentService.findByCourse(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create an assignment
	 *
	 * @param assignmentDTO the assignment to create
	 * @return the ResponseEntity with status 200 (OK) and the created assignment in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid final AssignmentDTO assignmentDTO) throws URISyntaxException {
		log.debug("POST request to create assignment : {}", assignmentDTO);
		AssignmentDTO response = assignmentService.save(assignmentDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update an assignment
	 *
	 * @param assignmentDTO the assignment to update
	 * @return the ResponseEntity with status 200 (OK) and the updated assignment in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid final AssignmentDTO assignmentDTO) {
		log.debug("PUT request to update assignment : {}", assignmentDTO);
		AssignmentDTO response = assignmentService.save(assignmentDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete an assignment
	 *
	 * @param id the ID of the assignment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete assignment : {}", id);
		assignmentService.delete(id);
		return ResponseEntity.ok().build();
	}



	/**
	 * POST  / : upload a course file
	 *
	 * @return the ResponseEntity with status 200 (OK) and the created course in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping(path="/{assignmentId}/upload")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public String uploadCourseFile(@PathVariable final Long assignmentId,
								   @RequestParam("file") MultipartFile file,
								   RedirectAttributes redirectAttributes) throws URISyntaxException {
		storageService.store(file, assignmentId, null);
		redirectAttributes.addFlashAttribute("message",
			"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "You successfully uploaded " + file.getOriginalFilename() + "!";
//		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	@GetMapping(path="/{assignmentId}/uploads")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUploads(@PathVariable final Long assignmentId) {
		log.debug("GET request to get course uploads for assignment " + assignmentId);
		List<S3ObjectSummary> response = storageService.getUploads(assignmentId, null).getObjectSummaries();
		return ResponseEntity.ok(response);
	}

	@GetMapping(path="/{assignmentId}/upload/{keyName:.+}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUpload(@PathVariable final Long assignmentId,
									@PathVariable final String keyName) {
		log.debug("GET request to get course upload : {}", keyName);
		InputStream response = storageService.getUpload(assignmentId, null, keyName);
		if (response != null) {
			try {
				byte[] out = org.apache.commons.io.IOUtils.toByteArray(response);

				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.add("content-disposition", "attachment; filename=" + keyName);
				// responseHeaders.add("Content-Type", type);

				return new ResponseEntity(out, responseHeaders, HttpStatus.OK);
			} catch (Exception e) {
				new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity ("File Not Found", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(path="/{assignmentId}/upload/{keyName:.+}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity deleteUpload(@PathVariable final Long assignmentId,
									   @PathVariable final String keyName) {
		try {
			storageService.deleteUpload(assignmentId, null, keyName);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting upload", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.OK);
	}
}
