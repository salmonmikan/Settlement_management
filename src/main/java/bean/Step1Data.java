package bean;

import java.io.Serializable;

/**
 * 出張申請のStep1（基本情報入力）で使用されるデータを保持するBeanクラス。
 * 出発日、帰着日、日数、プロジェクトコード、報告内容などを保持します。
 */
public class Step1Data implements Serializable {
    private static final long serialVersionUID = 1L;

    private String startDate;
    private String endDate;
    private int totalDays;
    private String projectCode;
    private String tripReport;

    // --- Getters and Setters ---

    /**
     * 出発日を取得します。
     * @return 出発日（文字列形式）
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 出発日を設定します。
     * @param startDate 出発日（文字列形式）
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 帰着日を取得します。
     * @return 帰着日（文字列形式）
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 帰着日を設定します。
     * @param endDate 帰着日（文字列形式）
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
    public String getTripReport() {
        return tripReport;
    }

    /**
     * 出張報告内容を設定します。
     * @param tripReport 出張報告
     */
    public void setTripReport(String tripReport) {
        this.tripReport = tripReport;
    }
}
