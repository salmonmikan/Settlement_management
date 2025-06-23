package model;

import java.sql.Timestamp;

public class Application {
    private int applicationId;         // 申請ID
    private String applicationType;    // 申請種別
    private Timestamp applicationDate; // 申請日付
    private int amount;                // 金額
    private String status;             // ステータス

    // 👇 Thêm 2 dòng này để phục vụ hiển thị trên 承認一覧
    private String staffId;            // 社員ID
    private String staffName;          // 社員名

    // --- Getter & Setter ---
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public Timestamp getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Timestamp applicationDate) {
        this.applicationDate = applicationDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}