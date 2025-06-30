/**
 * 各種申請に関する情報を保持するモデルクラスです。
 * 申請ID、申請種別、申請日、金額、ステータスの各項目を管理します。
 */

package bean;

import java.sql.Timestamp;

public class Application {
    private int applicationId;         // 申請ID
    private String applicationType;    // 申請種別
    private Timestamp createdAt;       // 作成日時（旧: applicationDate）
    private int amount;                // 金額
    private String status;             // ステータス

    private String staffId;            // 承認者表示用（追加済みなら giữ nguyên）
    private String staffName;

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

