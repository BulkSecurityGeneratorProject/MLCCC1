package org.mlccc.cm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String newsletterUploadDir;
    private String mediaUploadDir;
    private String uploadedFileDir;
    private String keyFilePath;
    private String ivFilePath;
    private String btMerchantId;
    private String btPublicKey;
    private String btPrivateKey;
    private String btEnvironment;


    public String getNewsletterUploadDir() {
        return newsletterUploadDir;
    }

    public void setNewsletterUploadDir(String newsletterUploadDir) {
        this.newsletterUploadDir = newsletterUploadDir;
    }

    public String getUploadedFileDir() {
        return uploadedFileDir;
    }

    public void setUploadedFileDir(String uploadedFileDir) {
        this.uploadedFileDir = uploadedFileDir;
    }

    public String getMediaUploadDir() {
        return mediaUploadDir;
    }

    public void setMediaUploadDir(String mediaUploadDir) {
        this.mediaUploadDir = mediaUploadDir;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    public String getIvFilePath() {
        return ivFilePath;
    }

    public void setIvFilePath(String ivFilePath) {
        this.ivFilePath = ivFilePath;
    }

    public String getBtMerchantId() {
        return btMerchantId;
    }

    public void setBtMerchantId(String btMerchantId) {
        this.btMerchantId = btMerchantId;
    }

    public String getBtPublicKey() {
        return btPublicKey;
    }

    public void setBtPublicKey(String btPublicKey) {
        this.btPublicKey = btPublicKey;
    }

    public String getBtPrivateKey() {
        return btPrivateKey;
    }

    public void setBtPrivateKey(String btPrivateKey) {
        this.btPrivateKey = btPrivateKey;
    }

    public String getBtEnvironment() {
        return btEnvironment;
    }

    public void setBtEnvironment(String btEnvironment) {
        this.btEnvironment = btEnvironment;
    }
}
