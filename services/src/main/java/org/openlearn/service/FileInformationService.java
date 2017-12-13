package org.openlearn.service;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.openlearn.dto.FileInformationDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.FileRepository;
import org.openlearn.repository.PortfolioItemRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.FileInformationTransformer;
import org.openlearn.web.rest.errors.AssignmentNotFoundException;
import org.openlearn.web.rest.errors.FileInformationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileInformationService {

	private static final Logger log = LoggerFactory.getLogger(FileInformationService.class);

	private final FileRepository fileRepository;

	private final AssignmentRepository assignmentRepository;

	private final PortfolioItemRepository portfolioItemRepository;

	private final UserService userService;

	private final FileInformationTransformer fileInformationTransformer;

	private final StorageService storageService;

	public FileInformationService(final FileRepository fileRepository,
								  final AssignmentRepository assignmentRepository,
								  final PortfolioItemRepository portfolioItemRepository,
								  final UserService userService,
								  final FileInformationTransformer fileInformationTransformer,
								  final StorageService storageService) {
		this.fileRepository = fileRepository;
		this.portfolioItemRepository = portfolioItemRepository;
		this.assignmentRepository = assignmentRepository;
		this.userService = userService;
		this.fileInformationTransformer = fileInformationTransformer;
		this.storageService = storageService;
	}

	/**
	 * Get all file information for a given assignment.
	 *
	 * @param assignmentId the id of the assignment
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<FileInformationDTO> findAllForAssignment(final Long assignmentId, Pageable pageable) {
		log.debug("Request to get File Information for Assignment : {}", assignmentId);
		Assignment assignment = assignmentRepository.findOne(assignmentId);
		if (assignment == null) throw new AssignmentNotFoundException(assignmentId);

		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT)) {
			return fileRepository.findByAssignmentAndUploadedByUser(assignment, userService.getCurrentUser(), pageable)
				.map(fileInformationTransformer::transform);
		} else {
			return fileRepository.findByAssignment(assignment, pageable)
				.map(fileInformationTransformer::transform);
		}
	}

	@Transactional(readOnly = true)
	public List<FileInformationDTO> findInstructorUploadsForAssignment(final Long assignmentId) {
		log.debug("Request to get File Information for Assignment : {}", assignmentId);
		Assignment assignment = assignmentRepository.findOne(assignmentId);
		if (assignment == null) throw new AssignmentNotFoundException(assignmentId);

		return fileRepository
			.findByAssignmentAndUploadedByUser(assignment, assignment.getCourse().getInstructor())
			.stream()
			.map(fileInformationTransformer::transform)
			.collect(Collectors.toList());
	}


	/**
	 * Get all file information for a given portfolio item.
	 *
	 * @param portfolioItemId the id of the portfolio item
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<FileInformationDTO> findAllForPorfolioItem(final Long portfolioItemId, Pageable pageable) {
		log.debug("Request to get File Information for PortfolioItem : {}", portfolioItemId);
		PortfolioItem portfolioItem = portfolioItemRepository.findOne(portfolioItemId);
		return fileRepository.findByPortfolioItem(portfolioItem, pageable).map(fileInformationTransformer::transform);
	}

	/**
	 * Get one file by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public FileInformationDTO findOne(final Long id) {
		log.debug("Request to get File Information : {}", id);
		return fileInformationTransformer.transform(fileRepository.findOne(id));
	}

	/**
	 *
	 * @param id
	 * @return the String base name of the file.
	 */
	@Transactional(readOnly = true)
	public String getFileNameFor(final Long id) {
		FileInformationDTO fileInformation = this.findOne(id);
		if (fileInformation == null) throw new FileInformationNotFoundException(id);

		return fileInformationTransformer.getFileBaseName(fileInformation);
	}

	/**
	 * Delete a file by portfolio
	 *
	 * @param portfolioItem the portfolio item whose files should be deleted.
	 */
	public void deleteByPortfolioItem(PortfolioItem portfolioItem) {
		storageService.deleteUploads(fileRepository.findByPortfolioItem(portfolioItem));
		fileRepository.deleteByPortfolioItem(portfolioItem);
	}

	/**
	 * Delete a file by assignment
	 *
	 * @param assignment the assignment whose files should be deleted.
	 */
	public void deleteByAssignment(Assignment assignment) {
		storageService.deleteUploads(fileRepository.findByAssignment(assignment));
		fileRepository.deleteByAssignment(assignment);
	}

	public Boolean isUploadedByCurrentUser(FileInformationDTO fileInformationDTO) {
		return isUploadedByUser(fileInformationDTO, userService.getCurrentUser());
	}

	public Boolean isUploadedByCourseInstructor(FileInformationDTO fileInformationDTO, Long assignmentId) {
		Assignment assignment = assignmentRepository.findOne(assignmentId);
		if (assignment == null) throw new AssignmentNotFoundException(assignmentId);

		return isUploadedByUser(fileInformationDTO, assignment.getCourse().getInstructor());
	}

	private Boolean isUploadedByUser(FileInformationDTO fileInformationDTO, User user) {
		return fileInformationDTO.getUploadedByUserId().equals(user);
	}
}
