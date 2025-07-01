package bean;

/**
 * プロジェクト管理情報を保持するBeanクラス。
 * <p>
 * プロジェクトコード、名称、責任者、メンバー、期間、予算、実績などを格納します。
 */
public class ProjectList {
    private String project_code;
    private String project_name;
    private String project_owner;
    private String project_members;
    private String start_date;
    private String end_date;
    private Integer project_budget;
    private Integer project_actual;

    /**
     * プロジェクトコードを取得します。
     * 
     * @return プロジェクトコード
     */
    public String getProject_code() {
        return project_code;
    }

    /**
     * プロジェクトコードを設定します。
     * 
     * @param project_code プロジェクトコード
     */
    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    /**
     * プロジェクト名を取得します。
     * 
     * @return プロジェクト名
     */
    public String getProject_name() {
        return project_name;
    }

    /**
     * プロジェクト名を設定します。
     * 
     * @param project_name プロジェクト名
     */
    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    /**
     * プロジェクト責任者を取得します。
     * 
     * @return プロジェクト責任者
     */
    public String getProject_owner() {
        return project_owner;
    }

    /**
     * プロジェクト責任者を設定します。
     * 
     * @param project_owner プロジェクト責任者
     */
    public void setProject_owner(String project_owner) {
        this.project_owner = project_owner;
    }

    /**
     * プロジェクトメンバーを取得します。
     * 
     * @return プロジェクトメンバー（カンマ区切り等）
     */
    public String getProject_members() {
        return project_members;
    }

    /**
     * プロジェクトメンバーを設定します。
     * 
     * @param project_members プロジェクトメンバー
     */
    public void setProject_members(String project_members) {
        this.project_members = project_members;
    }

    /**
     * プロジェクトの開始日を取得します。
     * 
     * @return プロジェクト開始日（文字列形式）
     */
    public String getStart_date() {
        return start_date;
    }

    /**
     * プロジェクトの開始日を設定します。
     * 
     * @param start_date プロジェクト開始日
     */
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    /**
     * プロジェクトの終了日を取得します。
     * 
     * @return プロジェクト終了日（文字列形式）
     */
    public String getEnd_date() {
        return end_date;
    }

    /**
     * プロジェクトの終了日を設定します。
     * 
     * @param end_date プロジェクト終了日
     */
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    /**
     * プロジェクトの実績金額を取得します。
     * 
     * @return 実績金額（単位：円など）
     */
    public Integer getProject_actual() {
        return project_actual;
    }

    /**
     * プロジェクトの実績金額を設定します。
     * 
     * @param project_actual 実績金額
     */
    public void setProject_actual(Integer project_actual) {
        this.project_actual = project_actual;
    }

    /**
     * プロジェクトの予算金額を取得します。
     * 
     * @return 予算金額（単位：円など）
     */
    public Integer getProject_budget() {
        return project_budget;
    }

    /**
     * プロジェクトの予算金額を設定します。
     * 
     * @param project_budget 予算金額
     */
    public void setProject_budget(Integer project_budget) {
        this.project_budget = project_budget;
    }
}
