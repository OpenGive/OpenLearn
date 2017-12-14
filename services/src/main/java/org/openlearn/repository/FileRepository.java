package org.openlearn.repository;

import org.openlearn.domain.FileInformation;
import org.openlearn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInformation, Long>{
	List<FileInformation> findByUser(User user);
}
