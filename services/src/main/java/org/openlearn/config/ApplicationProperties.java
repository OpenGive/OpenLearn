package org.openlearn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	private String uploadBucket;

	public String getUploadBucket() {
		return uploadBucket;
	}

	public void setUploadBucket(String uploadBucket) {
		this.uploadBucket = uploadBucket;
	}
}
