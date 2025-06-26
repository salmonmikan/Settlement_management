package bean.BusinessTripBean;

import java.sql.Date;

public class Employee {
    private String employeeId;
    private String fullName;
    private String furigana;
    private Date birthDate;
    private String address;
    private Date joinDate;
    private String loginId;
    private String password;
    private String departmentId;
    private String positionId;
    private String departmentName;
    private String positionName;

    // --- Getter & Setter ---
    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFurigana() {
        return furigana;
    }
    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Date getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPositionId() {
        return positionId;
    }
    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    // ✅ Getter/Setter cho tên 部署/役職 hiển thị
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
