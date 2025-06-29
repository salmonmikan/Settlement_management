package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Receipt implements Serializable {
    private int id; // receipt_id
    private String originalFileName;
    private String storedFilePath;
    private LocalDateTime uploadedAt;

    // Constructors
    public Receipt() {}

    public Receipt(String originalFileName, String storedFilePath) {
        this.originalFileName = originalFileName;
        this.storedFilePath = storedFilePath;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFilePath() {
        return storedFilePath;
    }

    public void setStoredFilePath(String storedFilePath) {
        this.storedFilePath = storedFilePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}