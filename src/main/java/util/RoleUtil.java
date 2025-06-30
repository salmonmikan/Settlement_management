// src/util/RoleUtil.java
package util;

/**
 * RoleUtil クラスは、社員の役職IDと部署IDに基づいて、
 * アプリケーション内でのロール（役割）を判定するユーティリティクラスです。
 */
public class RoleUtil {

    // --- Định nghĩa các hằng số để loại bỏ "Magic Strings" ---
    private static final String POSITION_MANAGEMENT = "P0001";
    private static final String POSITION_BUCHO = "P0002";
    private static final String POSITION_STAFF = "P0004";
    
    private static final String DEPARTMENT_MANAGEMENT = "D0002"; // Phòng kế toán/quản lý
    // Thêm các department khác nếu cần

    /**
     * アプリケーションで使用されるユーザーロールを表す列挙型です。
     */
    public enum UserRole {
        STAFF,          // nhân viên thường (gửi đơn)
        BUCHO,          // trưởng phòng (duyệt đơn)
        MANAGEMENT,     // quản lý (xem đơn đã duyệt)
        UNKNOWN         // không xác định
    }

    /**
     * 社員の役職IDと部署IDに基づいて、ユーザーのロールを判定します。
     *
     * @param positionId 社員の役職ID
     * @param departmentId 社員の部署ID
     * @return 該当するユーザーロール（{@link UserRole}）
     */
    public static UserRole detectRole(String positionId, String departmentId) {
        // Sử dụng hằng số giúp code dễ đọc và dễ bảo trì hơn rất nhiều
        if (POSITION_STAFF.equals(positionId)) {
            return UserRole.STAFF; // nhân viên thường (có thể gửi đơn)
        }

        if (POSITION_BUCHO.equals(positionId)) {
            return UserRole.BUCHO; // trưởng phòng (duyệt đơn của cấp dưới)
        }

        if (POSITION_MANAGEMENT.equals(positionId) && DEPARTMENT_MANAGEMENT.equals(departmentId)) {
            return UserRole.MANAGEMENT; // quản lý phòng kế toán (quản lý đơn đã duyệt)
        }

        return UserRole.UNKNOWN;
    }
}