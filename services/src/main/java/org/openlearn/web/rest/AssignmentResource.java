package org.openlearn.web.rest;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import io.swagger.annotations.ApiParam;
import org.openlearn.domain.Assignment;
import org.openlearn.domain.FileInformation;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.dto.FileInformationDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.AssignmentService;
import org.openlearn.service.CourseService;
import org.openlearn.service.FileInformationService;
import org.openlearn.service.StorageService;
import org.openlearn.web.rest.errors.AssignmentNotFoundException;
import org.openlearn.web.rest.errors.FileInformationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	private final FileInformationService fileInformationService;

	public AssignmentResource(final AssignmentService assignmentService,
							  final FileInformationService fileInformationService,
							  final StorageService storageService) {
		this.assignmentService = assignmentService;
		this.fileInformationService = fileInformationService;
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
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of assignments in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam final Pageable pageable) {
		log.debug("GET request for all assignments");
		Page<AssignmentDTO> response = assignmentService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
	}

	/**
	 * GET  / : get a list of all assignments for a course
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of assignments in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/course/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity getByCourse(@PathVariable final Long id, @ApiParam final Pageable pageable) {
		log.debug("GET request for all assignments by course: {}", id);
		Page<AssignmentDTO> response = assignmentService.findByCourse(id, pageable);
		return ResponseEntity.ok(response.getContent());
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
		if (hasCreateUpdateDeleteAuthority(assignmentDTO)) {
			AssignmentDTO response = assignmentService.save(assignmentDTO);
			return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
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
		if (hasCreateUpdateDeleteAuthority(assignmentDTO)) {
			AssignmentDTO response = assignmentService.save(assignmentDTO);
			return ResponseEntity.ok(response);
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
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
		if (hasCreateUpdateDeleteAuthority(assignmentService.findOne(id))) {
			assignmentService.delete(id);
			return ResponseEntity.ok().build();
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
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
	public ResponseEntity uploadCourseFile(@PathVariable final Long assignmentId,
								   @RequestParam("file") MultipartFile file,
								   RedirectAttributes redirectAttributes) throws URISyntaxException {
		if (canUploadFilesToAssignment(assignmentService.findOne(assignmentId))) {
			FileInformation response = storageService.store(file, assignmentId, null);
			redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

			URI location = new URI(ENDPOINT + response.getId());
			return ResponseEntity.created(location).body(response);
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(path="/{assignmentId}/uploads")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUploads(@PathVariable final Long assignmentId, @ApiParam final Pageable pageable) {
		log.debug("GET request to get course uploads for assignment " + assignmentId);
		if (canUploadFilesToAssignment(assignmentService.findOne(assignmentId))) {
			Page<FileInformationDTO> response = fileInformationService.findAllForAssignment(assignmentId, pageable);
			return ResponseEntity.ok(response.getContent());
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(path="/{assignmentId}/upload/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUpload(@PathVariable final Long assignmentId,
									@PathVariable final Long id) {
		log.debug("GET request to get course upload : {}", id);
		if (hasCreateUpdateDeleteAuthority(assignmentService.findOne(assignmentId))) {
			String fileName = fileInformationService.getFileNameFor(id);
			InputStream response = storageService.getUpload(id);
			if (response != null) {
				try {
					byte[] out = org.apache.commons.io.IOUtils.toByteArray(response);

					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.add("content-disposition", "attachment; filename=" + fileName);
					// responseHeaders.add("Content-Type", type);

					return new ResponseEntity(out, responseHeaders, HttpStatus.OK);
				} catch (Exception e) {
					new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
				}
				return new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping(path="/{assignmentId}/upload/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity deleteUpload(@PathVariable final Long assignmentId,
									   @PathVariable final Long id) {
		AssignmentDTO assignmentDTO = assignmentService.findOne(assignmentId);
		FileInformationDTO fileInformationDTO = fileInformationService.findOne(id);

		if (assignmentDTO == null) throw new AssignmentNotFoundException(assignmentId);
		if (fileInformationDTO == null) throw new FileInformationNotFoundException(id);

		if (hasCreateUpdateDeleteAuthority(assignmentDTO)) {
			try {
				storageService.deleteUpload(id);
			} catch (Exception e) {
				return new ResponseEntity("Error deleting upload", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity(HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	private Boolean canUploadFilesToAssignment(AssignmentDTO assignmentDTO) {
		return SecurityUtils.isAdmin() ||
			isOrgAdmin(assignmentDTO) ||
			isCourseInstructor(assignmentDTO) ||
			isCourseStudent(assignmentDTO);
	}

	private Boolean hasCreateUpdateDeleteAuthority(AssignmentDTO assignmentDTO) {
		return SecurityUtils.isAdmin() ||
			isOrgAdmin(assignmentDTO) ||
			isCourseInstructor(assignmentDTO);
	}

	private Boolean isOrgAdmin(AssignmentDTO assignmentDTO) {
		return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORG_ADMIN) &&
			assignmentService.inOrgOfCurrentUser(assignmentDTO);

	}

	private Boolean isCourseInstructor(AssignmentDTO assignmentDTO) {
		return assignmentService.currentUserIsCourseInstructor(assignmentDTO);
	}

	private Boolean isCourseStudent(AssignmentDTO assignmentDTO) {
		return assignmentService.currentUserIsEnrolledIn(assignmentDTO);
	}
}
