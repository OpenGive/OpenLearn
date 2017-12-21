package org.openlearn.client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import org.openlearn.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class S3Client {
	private static final Logger log = LoggerFactory.getLogger(S3Client.class);

	private AmazonS3 client;

	private boolean useCustomKmsKey;

	private String kmsKey;

	public S3Client(ApplicationProperties properties) {
		this.client = AmazonS3ClientBuilder.defaultClient();

		if (properties.getUploadKmsAlias() != null) {
			this.kmsKey = properties.getUploadKmsAlias();
			this.useCustomKmsKey = true;
		} else {
			this.useCustomKmsKey = false;
		}
	}

	public void putObject(PutObjectRequest request)  throws AmazonServiceException {
		if (useCustomKmsKey) {
			request.withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(this.kmsKey));
		} else {
			ObjectMetadata metadata = request.getMetadata();
			metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
		}

		this.client.putObject(request);
	}

	public S3Object getObject(GetObjectRequest request)  throws AmazonServiceException {
		return this.client.getObject(request);
	}

	public void deleteObject(DeleteObjectRequest request) throws AmazonServiceException {
		this.client.deleteObject(request);
	}

	public void deleteObjects(DeleteObjectsRequest request) throws AmazonServiceException {
		this.client.deleteObjects(request);
	}
}
