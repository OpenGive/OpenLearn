package org.openlearn.repository;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.FileInformation;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInformation, Long>{
	List<FileInformation> findByUser(User user);
	List<FileInformation> findByUploadedByUser(User uploadedByUser);
	List<FileInformation> findByAssignment(Assignment assignment);
	List<FileInformation> findByPortfolioItem(PortfolioItem portfolioItem);
	List<FileInformation> findByAssignmentAndUploadedByUser(Assignment assignment, User uploadedByUser);

	void deleteByPortfolioItem(PortfolioItem portfolioItem);
	void deleteByAssignment(Assignment assignment);
}
