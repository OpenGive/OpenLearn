package org.openlearn.web.rest;

import org.openlearn.dto.FileInformationDTO;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.FileInformationService;
import org.openlearn.service.PortfolioItemService;
import org.openlearn.service.StorageService;
import org.openlearn.service.UserService;
import org.openlearn.web.rest.errors.AccessDeniedException;
import org.openlearn.web.rest.errors.FileInformationNotFoundException;
import org.openlearn.web.rest.errors.PortfolioItemNotFoundException;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

// TODO enable these endpoints by uncommenting these annotations once better support for creating portfolio items exists
//@RestController
//@RequestMapping("/api/portfolio-items")
public class PortfolioItemResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/portfolio-items/";

	private static final Logger log = LoggerFactory.getLogger(PortfolioItemResource.class);

	private final PortfolioItemService portfolioItemService;

	private final StorageService storageService;

	private final UserService userService;

	private final FileInformationService fileInformationService;

	public PortfolioItemResource(final PortfolioItemService portfolioItemService, final StorageService storageService,
								 final UserService userService,
								 final FileInformationService fileInformationService) {
		this.portfolioItemService = portfolioItemService;
		this.storageService = storageService;
		this.userService = userService;
		this.fileInformationService = fileInformationService;
	}

	/**
	 * GET  /:id : get a single portfolio item by ID
	 *
	 * @param id the ID of the portfolio item to get
	 * @return the ResponseEntity with status 200 (OK) and the portfolio item in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get portfolio item : {}", id);
		PortfolioItemDTO response = portfolioItemService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all portfolio item, filtered by organization
	 *
	 * @return the ResponseEntity with status 200 (OK) and a list of portfolio items in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get() {
		log.debug("GET request for all portfolio items");
		List<PortfolioItemDTO> response = portfolioItemService.findAll();
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  /portfolio/:id : get a list of all portfolio items and flagged courses and assignments
	 *
	 * @param id of the student for which to retrieve portfolio
	 * @return the ResponseEntity with status 200 (OK) and a list of portfolio items in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/portfolio/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getPortfolio(@PathVariable final Long id) {
		log.debug("GET request for portfolio of student : {}", id);

		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT) &&
			!userService.getCurrentUser().getId().equals(id)) {

			log.error("Access denied for GET request for portfolio of student: {}", id);
			throw new AccessDeniedException();
		}

		List<PortfolioItemDTO> response = portfolioItemService.getPortfolioForStudent(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a portfolio item
	 *
	 * @param portfolioItemDTO the portfolio item to create
	 * @return the ResponseEntity with status 200 (OK) and the created portfolio item in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid final PortfolioItemDTO portfolioItemDTO) throws URISyntaxException {
		log.debug("POST request to create portfolio item : {}", portfolioItemDTO);
		if (hasCreateUpdateDeleteAuthority(portfolioItemDTO)) {
			PortfolioItemDTO response = portfolioItemService.save(portfolioItemDTO);
			return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
		} else {
			log.info("User is not authorized to create portfolio items");
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}

	}

	/**
	 * PUT  / : update a portfolio item
	 *
	 * @param portfolioItemDTO the portfolio item to update
	 * @return the ResponseEntity with status 200 (OK) and the updated portfolio item in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid final PortfolioItemDTO portfolioItemDTO) {
		log.debug("PUT request to update portfolio item : {}", portfolioItemDTO);
		if (hasCreateUpdateDeleteAuthority(portfolioItemDTO)) {
			PortfolioItemDTO response = portfolioItemService.save(portfolioItemDTO);
			return ResponseEntity.ok(response);
		} else {
			log.info("User is not authorized to update portfolio item: {}.", portfolioItemDTO.getId());
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * DELETE  / : delete a portfolio item
	 *
	 * @param id the ID of the portfolio item to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete portfolio item : {}", id);
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(id);
		if (hasCreateUpdateDeleteAuthority(portfolioItem)) {
			portfolioItemService.delete(id);
			return ResponseEntity.ok().build();
		} else {
			log.info("User is not authorized to delete portfolio item: {}.", id);
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
	@PostMapping(path="/{portfolioId}/upload")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity uploadCourseFile(@PathVariable final Long portfolioId,
										@RequestParam("file") MultipartFile file,
										RedirectAttributes redirectAttributes) throws URISyntaxException {
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(portfolioId);
		if (canUploadFilesToPortfolio(portfolioItem)) {
			FileInformationDTO response = storageService.store(file, null, portfolioId);
			redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");
			return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
		} else {
			log.info("User is not authorized to upload files for portfolio item: {}.", portfolioId);
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(path="/{portfolioId}/uploads")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUploads(@PathVariable final Long portfolioId) {
		log.debug("GET request to get course uploads for portfolio " + portfolioId);
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(portfolioId);
		if (portfolioItem == null) throw new PortfolioItemNotFoundException(portfolioId);

		if (canUploadFilesToPortfolio(portfolioItem)) {
			List<FileInformationDTO> response = fileInformationService.findAllForPorfolioItem(portfolioId);
			return ResponseEntity.ok(response);
		} else {
			log.info("User is not authorized to retrieve uploaded files for portfolio item {}.", portfolioId);
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(path="/{portfolioId}/upload/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUpload(@PathVariable final Long portfolioId,
									@PathVariable final Long id) {
		log.debug("GET request to get portfolio upload : {}", id);
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(portfolioId);
		FileInformationDTO fileInformationDTO = fileInformationService.findOne(id);

		if (portfolioItem == null) throw new PortfolioItemNotFoundException(portfolioId);
		if (fileInformationDTO == null) throw new FileInformationNotFoundException(id);

		if (canUploadFilesToPortfolio(portfolioItem) && fileInformationDTO.getPortfolioItemId().equals(portfolioId)) {
			String fileName = fileInformationService.getFileNameFor(id);
			InputStream response = storageService.getUpload(id);
			try {
				byte[] out = org.apache.commons.io.IOUtils.toByteArray(response);

				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.add("content-disposition", "attachment; filename=" + fileName);

				return new ResponseEntity(out, responseHeaders, HttpStatus.OK);
			} catch (IOException e) {
				log.error(e.getMessage());
				log.error("File entity '{}' could not be converted to a Byte Array.", id);
				return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			log.info("User is not authorized to retrieve uploaded file {}.", id);
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping(path="/{portfolioId}/upload/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity deleteUpload(@PathVariable final Long portfolioId,
									   @PathVariable final Long id) {
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(portfolioId);
		FileInformationDTO fileInformationDTO = fileInformationService.findOne(id);

		if (portfolioItem == null) throw new PortfolioItemNotFoundException(portfolioId);
		if (fileInformationDTO == null) throw new FileInformationNotFoundException(id);

		if (canUploadFilesToPortfolio(portfolioItem) && fileInformationDTO.getPortfolioItemId().equals(portfolioId)) {
			try {
				storageService.deleteUpload(id);
			} catch (Exception e) {
				log.error(e.getMessage());
				return new ResponseEntity("Error deleting upload", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity(HttpStatus.OK);
		} else {
			log.info("User is not authorized to delete uploaded file {}.", id);
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}

	private Boolean canUploadFilesToPortfolio(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isAdmin() ||
			isOrgAdmin(portfolioItem);
	}

	private Boolean hasCreateUpdateDeleteAuthority(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isAdmin() ||
			isOrgAdmin(portfolioItem);
	}

	private Boolean isOrgAdmin(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORG_ADMIN) &&
			portfolioItemService.isOrgAdminOfCurrentUser(portfolioItem);

	}

	private Boolean isPortfolioStudent(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT) &&
			portfolioItem.getStudentId().equals(userService.getCurrentUser().getId());
	}
}
