package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import bean.Step2Detail;

public class AllowanceDetailDAO {

    /**
     * 日当・宿泊費明細をデータベースに挿入する。
     * (Chèn một chi tiết phụ cấp/chỗ ở vào DB)
     * @param detail - Đối tượng Step2Detail
     * @param tripApplicationId - ID từ bảng business_trip_application
     * @param conn - Đối tượng Connection
     * @return detail_id vừa được tạo
     * @throws SQLException
     */
    public int insert(Step2Detail detail, int tripApplicationId, Connection conn) throws SQLException {
        String sql = "INSERT INTO allowance_detail (trip_application_id, region_type, trip_type, hotel, burden, hotel_fee, daily_allowance, days, expense_total, memo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
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
}