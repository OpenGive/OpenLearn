package org.openlearn.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.openlearn.config.ApplicationProperties;
import org.openlearn.domain.*;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.FileRepository;
import org.openlearn.repository.PortfolioItemRepository;
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

	private final AssignmentRepository assignmentRepository;

	private final UserService userService;

	private final PortfolioItemRepository portfolioItemRepository;

	@Autowired
	private ApplicationProperties props;

	public StorageService(final FileRepository fileRepository,
			final CourseRepository courseRepository,
			final AssignmentRepository assignmentRepository,
			final UserService userService,
	        final PortfolioItemRepository portfolioItemRepository) {
		this.fileRepository = fileRepository;
		this.courseRepository = courseRepository;
		this.assignmentRepository = assignmentRepository;
		this.userService = userService;
		this.portfolioItemRepository = portfolioItemRepository;
	}

	/**
	 * Store a file.
	 *
	 * @return the persisted entity
	 */
	//TODO cbernal fix blue documentation
	public FileInformation store(final MultipartFile file, Long assignmentId, Long portfolioId) {
		log.debug("Request to save f : {}", file); //TODO cbernal fix this log statement

		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
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


		User user = userService.getCurrentUser();
		FileInformation fileInformation = new FileInformation();
		fileInformation.setFileUrl("https://s3.amazonaws.com/" + uploadBucketName + "/" + keyName);
		fileInformation.setUploadedByUser(user);
		if (assignmentId != null) {
			Assignment assignment = assignmentRepository.findOne(assignmentId);
			Course course = assignment.getCourse();
			fileInformation.setCourse(course);
			fileInformation.setUser(course.getInstructor());
			fileInformation.setFileType("Course");
		} else {
			PortfolioItem portfolioItem = portfolioItemRepository.findOne(portfolioId);
			fileInformation.setUser(portfolioItem.getStudent());
			fileInformation.setFileType("Portfolio");
		}

		return fileRepository.save(fileInformation);
	}

	private static File convertToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		convertedFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(file.getBytes());
		fos.close();
		return convertedFile;
	}

	public ObjectListing getUploads(Long assignmentId, Long portfolioId) {
		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
		String uploadBucketName = props.getUploadBucket();
		log.debug("Getting uploads in " + uploadBucketName);
		try {
			return s3client.listObjects(uploadBucketName, getS3FilePrefix(assignmentId, portfolioId));
		} catch(Exception e) {
			log.error(e.getMessage());
			return new ObjectListing();
		}
	}

	public InputStream getUpload(Long assignmentId, Long portfolioId, String fileName) {
		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
		String uploadBucketName = props.getUploadBucket();
		log.debug("Getting uploads in " + uploadBucketName);
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
		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
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
