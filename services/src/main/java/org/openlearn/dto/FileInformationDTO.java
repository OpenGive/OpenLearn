package org.openlearn.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * A DTO representing a file information record
 */
public class FileInformationDTO {

	private Long id;

	@NotNull
	@Size(max = 200)
	private String fileUrl;

	@NotNull
	private ZonedDateTime createdDate;

	@NotNull
	private Long uploadedByUserId;

	@NotNull
	private Long userId;

	private String fileType;

	private Long assignmentId;

	private Long portfolioItemId;

	private AssignmentDTO assignment;

	private PortfolioItemDTO portfolioItem;

	private UserDTO uploadedByUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUploadedByUserId() {
		return uploadedByUserId;
	}

	public void setUploadedByUserId(Long uploadedByUserId) {
		this.uploadedByUserId = uploadedByUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Long getPortfolioItemId() {
		return portfolioItemId;
	}

	public void setPortfolioItemId(Long portfolioItemId) {
		this.portfolioItemId = portfolioItemId;
	}

	public AssignmentDTO getAssignment() {
		return assignment;
	}

	public void setAssignment(AssignmentDTO assignment) {
		this.assignment = assignment;
	}

	public PortfolioItemDTO getPortfolioItem() {
		return portfolioItem;
	}

	public void setPortfolioItem(PortfolioItemDTO portfolioItem) {
		this.portfolioItem = portfolioItem;
	}

	public UserDTO getUploadedByUser() {
		return uploadedByUser;
	}

	public void setUploadedByUser(UserDTO uploadedByUser) {
		this.uploadedByUser = uploadedByUser;
	}
}

