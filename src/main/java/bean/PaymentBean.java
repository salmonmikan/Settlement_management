package bean;

/**
 * 支払処理に関連する申請情報を保持するBeanクラス。
 * <p>
 * 申請ID、社員ID、申請種別、金額、申請日、ステータスなどの情報を扱います。
 */
public class PaymentBean {
    private int applicationId;
    private String staffId;
    private String staffName;
    private String applicationType;
    private String applicationDate;
    private int amount;
    private String status;

    /**
     * 申請IDを取得します。
     * @return 申請ID
     */
    public int getApplicationId() {
        return applicationId;
    }

    /**
     * 申請IDを設定します。
     * @param applicationId 申請ID
     */
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 社員IDを取得します。
     * @return 社員ID
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 社員IDを設定します。
     * @param staffId 社員ID
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * 社員氏名を取得します。
     * @return 社員氏名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 社員氏名を設定します。
     * @param staffName 社員氏名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * 申請種別を取得します。
     * @return 申請種別
     */
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * 申請種別を設定します。
     * @param applicationType 申請種別
     */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    /**
     * 申請日を取得します。
     * @return 申請日（文字列）
     */
    public String getApplicationDate() {
        return applicationDate;
    }

    /**
     * 申請日を設定します。
     * @param applicationDate 申請日（文字列）
     */
    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    /**
     * 金額を取得します。
     * @return 金額
     */
    public int getAmount() {
        return amount;
    }

    /**
     * 金額を設定します。
     * @param amount 金額
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * ステータスを取得します。
     * @return ステータス
     */
    public String getStatus() {
        return status;
    }

    /**
     * ステータスを設定します。
     * @param status ステータス
     */
    public void setStatus(String status) {
        this.status = status;
    }
}