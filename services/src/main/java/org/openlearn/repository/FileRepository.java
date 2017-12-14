package org.openlearn.repository;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.FileInformation;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInformation, Long>{
	List<FileInformation> findByUser(User user);
	Page<FileInformation> findByUser(User user, Pageable pageable);
	Page<FileInformation> findByUploadedByUser(User uploadedByUser, Pageable pageable);
	Page<FileInformation> findByAssignment(Assignment assignment, Pageable pageable);
	List<FileInformation> findByAssignment(Assignment assignment);
	Page<FileInformation> findByPortfolioItem(PortfolioItem portfolioItem, Pageable pageable);
	List<FileInformation> findByPortfolioItem(PortfolioItem portfolioItem);
	Page<FileInformation> findByAssignmentAndUploadedByUser(Assignment assignment, User uploadedByUser, Pageable pageable);
	List<FileInformation> findByAssignmentAndUploadedByUser(Assignment assignment, User uploadedByUser);

	void deleteByPortfolioItem(PortfolioItem portfolioItem);
	void deleteByAssignment(Assignment assignment);
}
