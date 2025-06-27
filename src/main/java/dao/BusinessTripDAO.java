package dao;

import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class BusinessTripDAO {

    // 1. Insert vào bảng application_header
    public int insertApplicationHeader(String staffId, String type, int amount) throws SQLException {
        String sql = "INSERT INTO application_header (staff_id, application_type, application_date, amount, status) " +
                     "VALUES (?, ?, NOW(), ?, '未提出')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, staffId);
            ps.setString(2, type);
            ps.setInt(3, amount);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // application_id
            }
        }
        throw new SQLException("出張申請のヘッダー登録に失敗しました");
    }

    // 2. Insert vào bảng business_trip_application
    public int insertBusinessTripApplication(int appId, Step1Data data) throws SQLException {
        String sql = "INSERT INTO business_trip_application (application_id, start_date, end_date, project_code, report, total_days) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, appId);
            ps.setDate(2, Date.valueOf(data.getStartDate()));
            ps.setDate(3, Date.valueOf(data.getEndDate()));
            ps.setString(4, data.getProjectCode());
            ps.setString(5, data.getReport());
            ps.setInt(6, data.getTotalDays());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // trip_application_id
            }
        }
        throw new SQLException("出張申請の明細登録に失敗しました");
    }

    // 3. Insert list allowance_detail
    public void insertAllowanceDetails(int tripId, List<Step2Detail> details) throws SQLException {
        String sql = "INSERT INTO allowance_detail (trip_application_id, region_type, trip_type, hotel, burden, hotel_fee, daily_allowance, days, expense_total, memo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Step2Detail d : details) {
                ps.setInt(1, tripId);
                ps.setString(2, d.getRegionType());
                ps.setString(3, d.getTripType());
                ps.setString(4, d.getHotel());
                ps.setString(5, d.getBurden());
                ps.setInt(6, d.getHotelFee());
                ps.setInt(7, d.getDailyAllowance());
                ps.setInt(8, d.getDays());
                ps.setInt(9, d.getExpenseTotal());
                ps.setString(10, d.getMemo());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // 4. Insert list transportation_detail
    public void insertTransportationDetails(int tripId, List<Step3Detail> details) throws SQLException {
        String sql = "INSERT INTO business_trip_transportation_detail (detail_id, trip_application_id, trans_project, departure, arrival, transport, fare_amount, trans_trip_type, trans_burden, trans_expense_total, trans_memo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idCounter = 1; // hoặc bạn có thể để auto-increment nếu schema sửa

            for (Step3Detail d : details) {
                ps.setInt(1, idCounter++); // hoặc tạo AUTO_INCREMENT mới trong schema
                ps.setInt(2, tripId);
                ps.setString(3, d.getTransProject());
                ps.setString(4, d.getDeparture());
                ps.setString(5, d.getArrival());
                ps.setString(6, d.getTransport());
                ps.setInt(7, d.getFareAmount());
                ps.setString(8, d.getTransTripType());
                ps.setString(9, d.getBurden());
                ps.setInt(10, d.getTransExpenseTotal());
                ps.setString(11, d.getMemo());

                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}