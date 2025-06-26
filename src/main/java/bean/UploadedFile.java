// Filename: src/main/java/bean/UploadedFile.java
package bean;

import java.io.Serializable;

public class UploadedFile implements Serializable {
    private static final long serialVersionUID = 1L; // Good practice for Serializable classes

    private String originalFileName; // Tên file gốc mà người dùng upload
    private String tempStoredPath;   // Đường dẫn tới file đã lưu tạm trên server
    private String mimeType;         // Kiểu file, ví dụ "image/jpeg"
    private long size;               // Kích thước file (bytes)

    public UploadedFile() {
        // Constructor mặc định
    }

    public UploadedFile(String originalFileName, String tempStoredPath, String mimeType, long size) {
        this.originalFileName = originalFileName;
        this.tempStoredPath = tempStoredPath;
        this.mimeType = mimeType;
        this.size = size;
    }

    // --- Getters & Setters ---

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getTempStoredPath() {
        return tempStoredPath;
    }

    public void setTempStoredPath(String tempStoredPath) {
        this.tempStoredPath = tempStoredPath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}