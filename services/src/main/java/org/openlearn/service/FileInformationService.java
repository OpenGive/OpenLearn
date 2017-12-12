package org.openlearn.service;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.FileInformation;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.dto.FileInformationDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.FileRepository;
import org.openlearn.repository.PortfolioItemRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.FileInformationTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT)) {
			return fileRepository.findByAssignmentAndUploadedByUser(assignment, userService.getCurrentUser(), pageable)
				.map(fileInformationTransformer::transform);
		} else {
			return fileRepository.findByAssignment(assignment, pageable)
				.map(fileInformationTransformer::transform);
		}
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

	@Transactional(readOnly = true)
	public String getFileNameFor(final Long id) {
		FileInformationDTO fileInformation = this.findOne(id);
		if (fileInformation != null) {
			return fileInformationTransformer.getFileBaseName(fileInformation);
		} else {
			return "";
		}
	}

	/**
	 * Delete a file by portfolio id
	 *
	 * @param portfolioItem the portfolio item whose files should be deleted.
	 */
	public void deleteByPortfolioItem(PortfolioItem portfolioItem) {
		storageService.deleteUploads(fileRepository.findByPortfolioItem(portfolioItem));
		fileRepository.deleteByPortfolioItem(portfolioItem);
	}
}
