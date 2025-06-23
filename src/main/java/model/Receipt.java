package model;

public class Receipt {
    private int receiptId;
    private String originalFileName;
    private String storedFilePath;

    // Getters & Setters
    public int getReceiptId() { return receiptId; }
    public void setReceiptId(int id) { this.receiptId = id; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String name) { this.originalFileName = name; }

    public String getStoredFilePath() { return storedFilePath; }
    public void setStoredFilePath(String path) { this.storedFilePath = path; }
}