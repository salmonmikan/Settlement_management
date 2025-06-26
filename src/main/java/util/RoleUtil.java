// src/util/RoleUtil.java
package util;

public class RoleUtil {
    public enum UserRole {
        STAFF,          // nhân viên thường (gửi đơn)
        BUCHO,          // trưởng phòng (duyệt đơn)
        MANAGEMENT,     // quản lý (xem đơn đã duyệt)
        UNKNOWN         // không xác định
    }

    public static UserRole detectRole(String positionId, String departmentId) {
        if ("P0004".equals(positionId)) {
            return UserRole.STAFF; // nhân viên thường (có thể gửi đơn)
        }

        if ("P0002".equals(positionId)) {
            return UserRole.BUCHO; // trưởng phòng (duyệt đơn của cấp dưới)
        }

        if ("P0001".equals(positionId) && "D0002".equals(departmentId)) {
            return UserRole.MANAGEMENT; // quản lý phòng kế toán (quản lý đơn đã duyệt)
        }

        return UserRole.UNKNOWN;
    }
} 
