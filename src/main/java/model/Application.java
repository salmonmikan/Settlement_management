package model;

import java.sql.Timestamp;

public class Application {
    private int applicationId;         // 申請ID
    private String applicationType;    // 申請種別
    private Timestamp applicationDate; // 申請時間
    private int amount;                // 金額
    private String status;            // ステータス

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
}