package bean;

import java.io.Serializable;

/**
 * 部署情報を保持するBeanクラス。
 * <p>
 * 各部署のID、名称、および削除フラグ（論理削除制御）を管理します。
 * 主に部署マスタの管理や一覧表示などに使用されます。
 */
public class DepartmentBean implements Serializable {
    private String department_id;
    private String department_name;
    private int delete_flag; // 削除フラグ（0=通常、1=論理削除、9=削除不可）

    /**
     * デフォルトコンストラクタ。
     */
    public DepartmentBean() {
    }

    /**
     * IDと名称を受け取るコンストラクタ。
     * @param department_id 部署ID
     * @param department_name 部署名
     */
    public DepartmentBean(String department_id, String department_name) {
        this.department_id = department_id;
        this.department_name = department_name;
    }

    /**
     * 部署IDを取得します。
     * @return 部署ID
     */
    public String getDepartment_id() {
        return department_id;
    }

    /**
     * 部署IDを設定します。
     * @param department_id 部署ID
     */
    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    /**
     * 部署名を取得します。
     * @return 部署名
     */
    public String getDepartment_name() {
        return department_name;
    }

    /**
     * 部署名を設定します。
     * @param department_name 部署名
     */
    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    /**
     * 削除フラグを取得します。
     * @return 削除フラグ（0=通常、1=論理削除、9=削除不可）
     */
    public int getDelete_flag() {
        return delete_flag;
    }

    /**
     * 削除フラグを設定します。
     * @param delete_flag 削除フラグ（0=通常、1=論理削除、9=削除不可）
     */
    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }
}