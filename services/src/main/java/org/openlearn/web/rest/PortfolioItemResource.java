package org.openlearn.web.rest;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import io.swagger.annotations.ApiParam;
import org.openlearn.domain.FileInformation;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.PortfolioItemService;
import org.openlearn.service.StorageService;
import org.openlearn.service.UserService;
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

import javax.swing.text.StyledEditorKit;
import javax.validation.Valid;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio-items")
public class PortfolioItemResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/portfolio-items/";

	private static final Logger log = LoggerFactory.getLogger(PortfolioItemResource.class);

	private final PortfolioItemService portfolioItemService;

	private final StorageService storageService;

	private final UserService userService;

	public PortfolioItemResource(final PortfolioItemService portfolioItemService, final StorageService storageService,
								 final UserService userService) {
		this.portfolioItemService = portfolioItemService;
		this.storageService = storageService;
		this.userService = userService;
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
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of portfolio items in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam final Pageable pageable) {
		log.debug("GET request for all portfolio items");
		Page<PortfolioItemDTO> response = portfolioItemService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
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
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
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
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
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
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(id);
		if (hasCreateUpdateDeleteAuthority(portfolioItem)) {
			log.debug("DELETE request to delete portfolio item : {}", id);
			portfolioItemService.delete(id);
			return ResponseEntity.ok().build();
		} else {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
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
			FileInformation response = storageService.store(file, null, portfolioId);
			redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");
			return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
		} else {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping(path="/{portfolioId}/uploads")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUploads(@PathVariable final Long portfolioId) {
		log.debug("GET request to get course uploads for portfolio " + portfolioId);
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(portfolioId);
		if (canUploadFilesToPortfolio(portfolioItem)) {
			List<S3ObjectSummary> response = storageService.getUploads(null, portfolioId).getObjectSummaries();
			return ResponseEntity.ok(response);
		} else {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping(path="/{portfolioId}/upload/{keyName:.+}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity getUpload(@PathVariable final Long portfolioId,
									@PathVariable final String keyName) {
		log.debug("GET request to get course upload : {}", keyName);
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(portfolioId);
		if (canUploadFilesToPortfolio(portfolioItem)) {
			InputStream response = storageService.getUpload(null, portfolioId, keyName);
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
				return new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
	}

	@DeleteMapping(path="/{portfolioId}/upload/{keyName:.+}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR, AuthoritiesConstants.STUDENT})
	public ResponseEntity deleteUpload(@PathVariable final Long portfolioId,
									   @PathVariable final String keyName) {
		PortfolioItemDTO portfolioItem = portfolioItemService.findOne(portfolioId);
		if (canUploadFilesToPortfolio(portfolioItem)) {
			try {
				storageService.deleteUpload(null, portfolioId, keyName);
			} catch (Exception e) {
				return new ResponseEntity("Error deleting upload", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity(HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
	}

	private Boolean canUploadFilesToPortfolio(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isAdmin() ||
			isOrgAdmin(portfolioItem) ||
			isPortfolioStudent(portfolioItem);
	}

	private Boolean hasCreateUpdateDeleteAuthority(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isAdmin() ||
			isOrgAdmin(portfolioItem) ||
			isPortfolioStudent(portfolioItem);
	}

	private Boolean isOrgAdmin(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ORG_ADMIN) &&
			portfolioItemService.inOrgOfCurrentUser(portfolioItem);

	}

	private Boolean isPortfolioStudent(PortfolioItemDTO portfolioItem) {
		return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT) &&
			portfolioItem.getStudentId() == userService.getCurrentUser().getId();
	}
}
