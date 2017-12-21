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

	private boolean requestServerSideEncryption;

	public S3Client(ApplicationProperties properties) {
		if (properties.getUploadKmsAlias() != null) {
			// Use custom client-side encryption method
			this.client = AmazonS3EncryptionClientBuilder
				.standard()
				.withCryptoConfiguration(new CryptoConfiguration(CryptoMode.EncryptionOnly))
				// Can either be Key ID or alias (prefixed with 'alias/')
				.withEncryptionMaterials(new KMSEncryptionMaterialsProvider(properties.getUploadKmsAlias()))
				.build();
			this.requestServerSideEncryption = false;
		} else {
			this.client = AmazonS3ClientBuilder.defaultClient();
			this.requestServerSideEncryption = true;
		}
	}

	public void putObject(PutObjectRequest request)  throws AmazonServiceException {
		if (requestServerSideEncryption) {
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
