package bean;

import java.security.MessageDigest;
import java.sql.Date;

/**
 * 社員情報を保持するBeanクラス。
 * <p>
 * 社員ID、氏名、ふりがな、生年月日、所属部署、役職など、
 * 社員マスタ情報に関わるすべての情報を保持します。
 */
public class Employee {
    private String employeeId;
    private String fullName;
    private String furigana;
    private Date birthDate;
    private String address;
    private Date joinDate;
    private String password;
    private String departmentId;
    private String positionId;
    private String departmentName;
    private String positionName;

    
    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = md.digest(password.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    /**
     * 社員IDを取得します。
     * @return 社員ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * 社員IDを設定します。
     * @param employeeId 社員ID
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * 氏名を取得します。
     * @return 氏名
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 氏名を設定します。
     * @param fullName 氏名
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * ふりがなを取得します。
     * @return ふりがな
     */
    public String getFurigana() {
        return furigana;
    }

    /**
     * ふりがなを設定します。
     * @param furigana ふりがな
     */
    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    /**
     * 生年月日を取得します。
     * @return 生年月日
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * 生年月日を設定します。
     * @param birthDate 生年月日
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * 住所を取得します。
     * @return 住所
     */
    public String getAddress() {
        return address;
    }

    /**
     * 住所を設定します。
     * @param address 住所
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 入社日を取得します。
     * @return 入社日
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * 入社日を設定します。
     * @param joinDate 入社日
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * パスワードを取得します。
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定します。
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 部署IDを取得します。
     * @return 部署ID
     */
    public String getDepartmentId() {
        return departmentId;
    }

    /**
     * 部署IDを設定します。
     * @param departmentId 部署ID
     */
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 役職IDを取得します。
     * @return 役職ID
     */
    public String getPositionId() {
        return positionId;
    }

    /**
     * 役職IDを設定します。
     * @param positionId 役職ID
     */
    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    /**
     * 部署名を取得します。
     * @return 部署名
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 部署名を設定します。
     * @param departmentName 部署名
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 役職名を取得します。
     * @return 役職名
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * 役職名を設定します。
     * @param positionName 役職名
     */
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
