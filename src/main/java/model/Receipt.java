package model;

public class Receipt {
    private int receiptId;
    private String originalFileName;
    private String storedFilePath;

    private String refTable;
    private int refId;

    private int applicationId;
    private String stepType; // step2, step3

    private int blockIndex; // ✅ Dùng để phân biệt block trong step2/3

    // Getter & Setter
    public int getReceiptId() { return receiptId; }
    public void setReceiptId(int id) { this.receiptId = id; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String name) { this.originalFileName = name; }

    public String getStoredFilePath() { return storedFilePath; }
    public void setStoredFilePath(String path) { this.storedFilePath = path; }

    public String getRefTable() { return refTable; }
    public void setRefTable(String refTable) { this.refTable = refTable; }

    public int getRefId() { return refId; }
    public void setRefId(int refId) { this.refId = refId; }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public String getStepType() { return stepType; }
    public void setStepType(String stepType) { this.stepType = stepType; }

    public int getBlockIndex() { return blockIndex; }
    public void setBlockIndex(int blockIndex) { this.blockIndex = blockIndex; }
}