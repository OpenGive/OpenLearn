package org.openlearn.service;

import java.util.Date;

import org.openlearn.domain.Course;
import org.openlearn.domain.FileInformation;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing File upload.
 */
@Service
@Transactional
public class StorageService {
	private static final Logger log = LoggerFactory.getLogger(AddressService.class);

	private final FileRepository fileRepository;
	
	private final CourseRepository courseRepository;
	
	private String baseUrl = "s3.amazon.com";
	
	public StorageService(final FileRepository fileRepository,
			final CourseRepository courseRepository) {
		this.fileRepository = fileRepository;
		this.courseRepository = courseRepository;
	}
	
	/**
	 * Store a file.
	 *
	 * @param address the entity to save
	 * @return the persisted entity
	 */
	//TODO cbernal fix blue documentation
	public FileInformation store(final MultipartFile file, Long courseId) {
		log.debug("Request to save f : {}", file); //TODO cbernal fix this log statement
		//TODO actually send file to s3
		
		Course course = courseRepository.findOne(courseId);
		
		FileInformation fileInformation = new FileInformation();
		fileInformation.setFileType("Course");
		fileInformation.setFileUrl("baseurl" + "course" + course.getId() + "-" + new Date().toString());
		fileInformation.setUser(course.getInstructor());
		return fileRepository.save(fileInformation);
	}
}
