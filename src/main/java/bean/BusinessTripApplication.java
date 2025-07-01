// File: src/main/java/bean/BusinessTripBean/BusinessTripApplication.java
package bean;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 出張申請の情報を管理するクラスです。
 * 出張期間、プロジェクト、報告内容、日数などの情報を保持します。
 */
public class BusinessTripApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tripApplicationId;
    private int applicationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectCode;
    private String report;
    private int totalDays;
    private String staffId;

    // --- Getters and Setters ---
    /**
     * 出張申請IDを取得します。
     * @return 出張申請ID
     */
    public int getTripApplicationId() {
        return tripApplicationId;
    }

    /**
     * 出張申請IDを設定します。
     * @param tripApplicationId 出張申請ID
     */
    public void setTripApplicationId(int tripApplicationId) {
        this.tripApplicationId = tripApplicationId;
    }

    /**
     * 紐づく申請IDを取得します。
     * @return 申請ID
     */
    public int getApplicationId() {
        return applicationId;
    }

    /**
     * 紐づく申請IDを設定します。
     * @param applicationId 申請ID
     */
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 出張開始日を取得します。
     * @return 出張開始日
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * 出張開始日を設定します。
     * @param startDate 出張開始日
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * 出張終了日を取得します。
     * @return 出張終了日
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * 出張終了日を設定します。
     * @param endDate 出張終了日
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * プロジェクトコードを取得します。
     * @return プロジェクトコード
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * プロジェクトコードを設定します。
     * @param projectCode プロジェクトコード
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 出張報告内容を取得します。
     * @return 出張報告
     */
    public String getReport() {
        return report;
    }

    /**
     * 出張報告内容を設定します。
     * @param report 出張報告
     */
    public void setReport(String report) {
        this.report = report;
    }

    /**
     * 出張日数を取得します。
     * @return 出張日数
     */
    public int getTotalDays() {
        return totalDays;
    }

    /**
     * 出張日数を設定します。
     * @param totalDays 出張日数
     */
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
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
}