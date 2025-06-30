package bean;

public class PaymentBean {
    private int applicationId;
    private String staffId;
    private String staffName;
    private String applicationType;
    private String applicationDate;
    private int amount;
    private String status;

    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public int getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
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

    public String getApplicationType() {
        return applicationType;
    }
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationDate() {
        return applicationDate;
    }
    public void setApplicationDate(String applicationDate) {
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