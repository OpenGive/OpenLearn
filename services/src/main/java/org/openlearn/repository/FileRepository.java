package org.openlearn.repository;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.FileInformation;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInformation, Long>{
	Page<FileInformation> findByUser(User user, Pageable pageable);
	Page<FileInformation> findByUploadedByUser(User uploadedByUser, Pageable pageable);
	Page<FileInformation> findByAssignment(Assignment assignment, Pageable pageable);
	Page<FileInformation> findByPortfolioItem(PortfolioItem portfolioItem, Pageable pageable);
	Page<FileInformation> findByAssignmentAndUploadedByUser(Assignment assignment, User uploadedByUser, Pageable pageable);
}
