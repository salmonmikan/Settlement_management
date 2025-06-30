package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private int applicationId;
    private String applicationType;
    private Timestamp applicationDate;
    private int amount;
    private String status;
    
    // ★★★ THÊM 2 THUỘC TÍNH NÀY VÀO ★★★
    private String staffId;
    private String staffName;
    
    private Timestamp createdAt;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    // --- Getters and Setters ---
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }
    
    public String getApplicationType() { return applicationType; }
    public void setApplicationType(String applicationType) { this.applicationType = applicationType; }
    
    public Timestamp getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Timestamp applicationDate) { this.applicationDate = applicationDate; }
    
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ★★★ THÊM CÁC GETTER/SETTER NÀY VÀO ★★★
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
}