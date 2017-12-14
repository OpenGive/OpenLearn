package org.openlearn.transformer;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.FileInformation;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.dto.FileInformationDTO;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.dto.UserDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.FileRepository;
import org.openlearn.repository.PortfolioItemRepository;
import org.openlearn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileInformationTransformer {

	private static final Logger log = LoggerFactory.getLogger(CourseTransformer.class);

	private FileRepository fileRepository;

	private AssignmentTransformer assignmentTransformer;

	private PortfolioItemTransformer portfolioItemTransformer;

	private UserRepository userRepository;

	private AssignmentRepository assignmentRepository;

	private PortfolioItemRepository portfolioItemRepository;

	private UserTransformer userTransformer;

	public FileInformationTransformer(final FileRepository fileRepository,
									  final AssignmentTransformer assignmentTransformer,
									  final PortfolioItemTransformer portfolioItemTransformer,
									  final UserRepository userRepository,
									  final AssignmentRepository assignmentRepository,
									  final PortfolioItemRepository portfolioItemRepository,
									  final UserTransformer userTransformer) {
		this.fileRepository = fileRepository;
		this.assignmentTransformer = assignmentTransformer;
		this.portfolioItemTransformer = portfolioItemTransformer;
		this.userRepository = userRepository;
		this.assignmentRepository = assignmentRepository;
		this.portfolioItemRepository = portfolioItemRepository;
		this.userTransformer = userTransformer;
	}

	public FileInformationDTO transform(FileInformation fileInformation) {
		log.debug("Transforming file information to file information DTO : {}", fileInformation);
		FileInformationDTO fileInformationDTO = new FileInformationDTO();
		fileInformationDTO.setId(fileInformation.getId());
		fileInformationDTO.setFileUrl(fileInformation.getFileUrl());
		fileInformationDTO.setFileType(fileInformation.getFileType());
		fileInformationDTO.setCreatedDate(fileInformation.getCreatedDate());
		fileInformationDTO.setUploadedByUserId(fileInformation.getUploadedByUser().getId());
		fileInformationDTO.setUserId(fileInformation.getUser().getId());

		UserDTO uploadedByUserDTO = new UserDTO();
		userTransformer.transformUserToDTO(uploadedByUserDTO, fileInformation.getUploadedByUser());
		fileInformationDTO.setUploadedByUser(uploadedByUserDTO);

		Assignment assignment = fileInformation.getAssignment();
		if (assignment != null) {
			fileInformationDTO.setAssignmentId(assignment.getId());
			fileInformationDTO.setAssignment(assignmentTransformer.transform(assignment));
		}

		PortfolioItem portfolioItem = fileInformation.getPortfolioItem();
		if (portfolioItem != null) {
			fileInformationDTO.setPortfolioItemId(portfolioItem.getId());
			fileInformationDTO.setPortfolioItem(portfolioItemTransformer.transform(portfolioItem));
		}

		return fileInformationDTO;
	}

	public FileInformation transform(FileInformationDTO fileInformationDTO) {
		log.debug("Transforming file information DTO to file information : {}", fileInformationDTO);
		FileInformation fileInformation = fileInformationDTO.getId() == null ? new FileInformation() : fileRepository.findOne(fileInformationDTO.getId());
		fileInformation.setId(fileInformationDTO.getId());
		fileInformation.setFileUrl(fileInformationDTO.getFileUrl());
		fileInformation.setFileType(fileInformationDTO.getFileType());
		fileInformation.setCreatedDate(fileInformationDTO.getCreatedDate());
		fileInformation.setUploadedByUser(userRepository.findOne(fileInformationDTO.getUploadedByUserId()));
		fileInformation.setUser(userRepository.findOne(fileInformationDTO.getUserId()));
		fileInformation.setAssignment(assignmentRepository.findOne(fileInformationDTO.getAssignmentId()));
		fileInformation.setPortfolioItem(portfolioItemRepository.findOne(fileInformationDTO.getPortfolioItemId()));

		return fileInformation;
	}

	public String getFileBaseName(FileInformation fileInformation) {
		return this.getFileBaseName(this.transform(fileInformation));
	}

	public String getFileBaseName(FileInformationDTO fileInformationDTO) {
		String fileUrl = fileInformationDTO.getFileUrl();
		String path = fileUrl.substring(fileUrl.indexOf("/"));
		return (new File(path)).getName();
	}
}
