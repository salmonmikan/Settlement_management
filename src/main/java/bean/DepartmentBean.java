package bean;

import java.io.Serializable;

public class DepartmentBean implements Serializable {
    private String department_id;
    private String department_name;
    private int delete_flag; // 削除フラグ追加（0=通常、1=論理削除、9=削除不可）

    public DepartmentBean() {
    }

    public DepartmentBean(String department_id, String department_name) {
        this.department_id = department_id;
        this.department_name = department_name;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public int getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }
}
