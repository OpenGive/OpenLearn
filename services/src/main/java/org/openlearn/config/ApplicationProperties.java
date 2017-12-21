package org.openlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Recaptcha recaptcha = new Recaptcha();

    private final Uploads uploads = new Uploads();

    public String getUploadBucket() {
        return this.uploads.s3bucket;
    }

    public void setUploadBucket(String uploadBucket) {
        this.uploads.setS3bucket(uploadBucket);
    }

    public Recaptcha getRecaptcha() {
        return recaptcha;
    }

    public Uploads getUploads() {
      return uploads;
    }

    public String getUploadKmsAlias() {
      return this.uploads.getKmsAlias();
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

    public static class Uploads {
      private String s3bucket;

      private String kmsAlias;

      public String getS3bucket() {
        return s3bucket;
      }

      public void setS3bucket(String s3bucket) {
        this.s3bucket = s3bucket;
      }

      public String getKmsAlias() {
        return kmsAlias;
      }

      public void setKmsAlias(String kmsAlias) {
        this.kmsAlias = kmsAlias;
      }

      @Override
      public String toString() {
        return super.toString() + "s3bucket=" + this.s3bucket + ";kmAlias=" + this.kmsAlias;
      }
    }
}
