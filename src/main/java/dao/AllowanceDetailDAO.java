package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.Step2Detail;

/**
 * Lớp DAO cho bảng allowance_detail (chi tiết phụ cấp/chỗ ở).
 * Cung cấp các thao tác CRUD với DB.
 */
public class AllowanceDetailDAO {

    /**
     * Chèn một chi tiết phụ cấp/chỗ ở vào DB, bao gồm cả các tùy chọn điều chỉnh.
     * @param detail Đối tượng Step2Detail chứa dữ liệu cần chèn.
     * @param tripApplicationId ID của đơn công tác cha.
     * @param conn Đối tượng Connection.
     * @return detail_id vừa được tạo.
     * @throws SQLException
     */
    public int insert(Step2Detail detail, int tripApplicationId, Connection conn) throws SQLException {
        // Cập nhật câu lệnh SQL để bao gồm cột 'adjustment_options'
        String sql = "INSERT INTO allowance_detail (trip_application_id, region_type, trip_type, hotel, burden, hotel_fee, daily_allowance, days, expense_total, memo, adjustment_options) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tripApplicationId);
            ps.setString(2, detail.getRegionType());
            ps.setString(3, detail.getTripType());
            ps.setString(4, detail.getHotel());
            ps.setString(5, detail.getBurden());
            ps.setInt(6, detail.getHotelFee());
            ps.setInt(7, detail.getDailyAllowance());
            ps.setInt(8, detail.getDays());
            ps.setInt(9, detail.getExpenseTotal());
            ps.setString(10, detail.getMemo());

            // Chuyển List<String> thành một chuỗi duy nhất, phân tách bằng dấu phẩy
            // Ví dụ: ["half_day", "bonus"] -> "half_day,bonus"
            String adjustmentOptionsStr = String.join(",", detail.getAdjustmentOptions());
            ps.setString(11, adjustmentOptionsStr);

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về detail_id
                } else {
                    throw new SQLException("Creating allowance detail failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Xóa tất cả các chi tiết phụ cấp thuộc về một đơn công tác.
     * Thường được dùng khi cập nhật đơn (xóa cũ, chèn mới).
     * @param tripApplicationId ID của đơn công tác.
     * @param conn Đối tượng Connection.
     * @throws SQLException
     */
    public void deleteByTripApplicationId(int tripApplicationId, Connection conn) throws SQLException {
        String sql = "DELETE FROM allowance_detail WHERE trip_application_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripApplicationId);
            ps.executeUpdate();
        }
    }

    /**
     * [PHƯƠNG THỨC MỚI]
     * Tìm tất cả các chi tiết phụ cấp dựa trên ID của đơn công tác.
     * Rất quan trọng cho chức năng sửa đơn.
     * @param tripApplicationId ID của đơn công tác.
     * @param conn Đối tượng Connection.
     * @return Danh sách các đối tượng Step2Detail.
     * @throws SQLException
     */
    public List<Step2Detail> findByTripApplicationId(int tripApplicationId, Connection conn) throws SQLException {
        List<Step2Detail> details = new ArrayList<>();
        // Cập nhật câu lệnh SELECT để lấy tất cả các cột
        String sql = "SELECT * FROM allowance_detail WHERE trip_application_id = ? ORDER BY detail_id ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripApplicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Step2Detail detail = new Step2Detail();
                    detail.setRegionType(rs.getString("region_type"));
                    detail.setTripType(rs.getString("trip_type"));
                    detail.setHotel(rs.getString("hotel"));
                    detail.setBurden(rs.getString("burden"));
                    detail.setHotelFee(rs.getInt("hotel_fee"));
                    detail.setDailyAllowance(rs.getInt("daily_allowance"));
                    detail.setDays(rs.getInt("days"));
                    detail.setExpenseTotal(rs.getInt("expense_total"));
                    detail.setMemo(rs.getString("memo"));

                    // Đọc chuỗi từ DB và chuyển đổi ngược lại thành List<String>
                    String adjustmentOptionsStr = rs.getString("adjustment_options");
                    if (adjustmentOptionsStr != null && !adjustmentOptionsStr.isEmpty()) {
                        detail.setAdjustmentOptions(Arrays.asList(adjustmentOptionsStr.split(",")));
                    } else {
                        detail.setAdjustmentOptions(new ArrayList<>()); // Trả về list rỗng nếu null
                    }
                    
                    details.add(detail);
                }
            }
        }
        return details;
    }
}