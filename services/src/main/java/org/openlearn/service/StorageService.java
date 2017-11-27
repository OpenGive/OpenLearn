package org.openlearn.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.openlearn.config.ApplicationProperties;
import org.openlearn.domain.Course;
import org.openlearn.domain.FileInformation;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing File upload.
 */
@Service
@Transactional
@EnableConfigurationProperties
public class StorageService {
	private static final Logger log = LoggerFactory.getLogger(AddressService.class);

	private final FileRepository fileRepository;

	private final CourseRepository courseRepository;

	@Autowired
	private ApplicationProperties props;

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

		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		String uploadBucketName = props.getUploadBucket();
		String keyName = courseId + "/" + file.getOriginalFilename();
		try {
			File f = convertToFile(file);
			s3client.putObject(new PutObjectRequest(uploadBucketName, keyName, f));
			f.delete();
		} catch (AmazonServiceException e) {
			log.error(e.getErrorMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}


		Course course = courseRepository.findOne(courseId);

		FileInformation fileInformation = new FileInformation();
		fileInformation.setFileType("Course");
		fileInformation.setFileUrl("https://s3.amazonaws.com/"+uploadBucketName+"/"+keyName);
		fileInformation.setUser(course.getInstructor());
		fileInformation.setCourse(course);

		return fileRepository.save(fileInformation);
	}

	private static File convertToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public ObjectListing get(Long courseId) {
		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		String uploadBucketName = props.getUploadBucket();
		return s3client.listObjects(uploadBucketName, courseId.toString());
	}

}
