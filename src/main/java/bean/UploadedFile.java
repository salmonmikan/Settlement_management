// Trong file bean/UploadedFile.java
package bean;

import java.io.Serializable;

public class UploadedFile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String originalFileName;
    private String uniqueStoredName;
    private String temporaryPath; 
    private String storedFilePath;
    
    // ★★★ THÊM THUỘC TÍNH NÀY VÀO ★★★
    private int blockIndex;

    // --- Getters and Setters ---
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    
    public String getUniqueStoredName() { return uniqueStoredName; }
    public void setUniqueStoredName(String uniqueStoredName) { this.uniqueStoredName = uniqueStoredName; }
    
    public String getTemporaryPath() { return temporaryPath; }
    public void setTemporaryPath(String temporaryPath) { this.temporaryPath = temporaryPath; }

    public String getStoredFilePath() { return storedFilePath; }
    public void setStoredFilePath(String storedFilePath) { this.storedFilePath = storedFilePath; }

    // ★★★ THÊM GETTER/SETTER NÀY VÀO ★★★
    public int getBlockIndex() { return blockIndex; }
    public void setBlockIndex(int blockIndex) { this.blockIndex = blockIndex; }
}