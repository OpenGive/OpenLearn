package org.openlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String uploadBucket;

    private final Recaptcha recaptcha = new Recaptcha();

    public String getUploadBucket() {
        return uploadBucket;
    }

    public void setUploadBucket(String uploadBucket) {
        this.uploadBucket = uploadBucket;
    }

    public Recaptcha getRecaptcha() {
        return recaptcha;
    }

    public static class Recaptcha {

        private String headerName;

        private String siteSecret;

        private String verificationUrl;

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }

        public String getSiteSecret() {
            return siteSecret;
        }

        public void setSiteSecret(String siteSecret) {
            this.siteSecret = siteSecret;
        }

        public String getVerificationUrl() {
            return verificationUrl;
        }

        public void setVerificationUrl(String verificationUrl) {
            this.verificationUrl = verificationUrl;
        }
    }
}
