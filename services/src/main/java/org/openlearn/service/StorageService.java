package org.openlearn.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

	private String uploadBucketName = "tapickens-openlearn-upload";

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

		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		try {
			String keyName = file.getOriginalFilename();
			File f = convertToFile(file);
			s3client.putObject(new PutObjectRequest(uploadBucketName, keyName, f));
		} catch (AmazonServiceException e) {
			log.error(e.getErrorMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}


		Course course = courseRepository.findOne(courseId);

		FileInformation fileInformation = new FileInformation();
		fileInformation.setFileType("Course");
		fileInformation.setFileUrl("baseurl" + "course" + course.getId() + "-" + new Date().toString());
		fileInformation.setUser(course.getInstructor());
		// TODO: commented out because this is not working
//		return fileRepository.save(fileInformation);
		return fileInformation;
	}

	private static File convertToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

}
