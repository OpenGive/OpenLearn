package org.openlearn.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.openlearn.config.ApplicationProperties;
import org.openlearn.domain.Course;
import org.openlearn.domain.FileInformation;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
	 * @return the persisted entity
	 */
	//TODO cbernal fix blue documentation
	public FileInformation store(final MultipartFile file, Long assignmentId, Long portfolioId) {
		log.debug("Request to save f : {}", file); //TODO cbernal fix this log statement

		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		String uploadBucketName = props.getUploadBucket();
		String keyName = getS3FilePrefix(assignmentId, portfolioId) + file.getOriginalFilename();
		try {
			File f = convertToFile(file);
			s3client.putObject(new PutObjectRequest(uploadBucketName, keyName, f));
			f.delete();
		} catch (AmazonServiceException e) {
			log.error(e.getErrorMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}


//		Course course = new Course();//courseRepository.findOne(courseId);

		FileInformation fileInformation = new FileInformation();
		fileInformation.setFileType("Course");
		fileInformation.setFileUrl("https://s3.amazonaws.com/"+uploadBucketName+"/"+keyName);
//		fileInformation.setUser(course.getInstructor());
//		fileInformation.setCourse(course);

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

	public ObjectListing getUploads(Long assignmentId, Long portfolioId) {
		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		String uploadBucketName = props.getUploadBucket();
		return s3client.listObjects(uploadBucketName, getS3FilePrefix(assignmentId, portfolioId));
	}

	public InputStream getUpload(Long assignmentId, Long portfolioId, String fileName) {
		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		String uploadBucketName = props.getUploadBucket();
		try {
			String key = getS3FilePrefix(assignmentId, portfolioId) + fileName;
			S3Object s3Object = s3client.getObject(new GetObjectRequest(uploadBucketName, key));
			InputStream objectData = s3Object.getObjectContent();
			return objectData;
		} catch(Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public void deleteUpload(Long assignmentId, Long portfolioId, String fileName) {
		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		String uploadBucketName = props.getUploadBucket();
		try {
			String key = getS3FilePrefix(assignmentId, portfolioId) + fileName;
			s3client.deleteObject(new DeleteObjectRequest(uploadBucketName, key));
		} catch(Exception e) {
			log.error(e.getMessage());
		}
	}

	private String getS3FilePrefix(Long assignmentId, Long portfolioId) {
		// s3 file name is a_[assignmentid]_[filename] for assignments and p_[portfolioid]_[filename] for portfolios
		String assignmentStr = assignmentId != null ? "a_" + assignmentId.toString() + "_" : "";
		String portfolioStr = portfolioId != null ? "p_" + portfolioId.toString() + "_" : "";
		String prefix = assignmentStr + portfolioStr;
		return prefix;
	}

}
