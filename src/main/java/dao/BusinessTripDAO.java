// ✅ BusinessTripDAO.java: xử lý DB cho bảng business_trip liên quan
package dao;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;

public class BusinessTripDAO {

    public int insertBusinessTripApplication(int applicationId, Step1Data s1) throws Exception {
        String sql = "INSERT INTO business_trip_application (application_id, start_date, end_date, project_code, report, total_days) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, applicationId);
            ps.setString(2, s1.getStartDate());
            ps.setString(3, s1.getEndDate());
            ps.setString(4, s1.getProjectCode());
            ps.setString(5, s1.getReport());
            ps.setString(6, s1.getTotalDays());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("Insert business_trip_application failed");
    }

    public int insertStep2Detail(int tripAppId, Step2Detail d) throws Exception {
        String sql = "INSERT INTO business_trip_detail (trip_application_id, step_type, region_type, trip_type, hotel, burden, hotel_fee, daily_allowance, days, expense_total, memo) " +
                     "VALUES (?, 'step2', ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tripAppId);
            ps.setString(2, d.getRegionType());
            ps.setString(3, d.getTripType());
            ps.setString(4, d.getHotel());
            ps.setString(5, d.getBurden());
            ps.setString(6, d.getHotelFee());
            ps.setString(7, d.getDailyAllowance());
            ps.setString(8, d.getDays());
            ps.setString(9, d.getExpenseTotal());
            ps.setString(10, d.getMemo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("Insert step2 detail failed");
    }

    public int insertStep3Detail(int tripAppId, Step3Detail d) throws Exception {
        String sql = "INSERT INTO business_trip_detail (trip_application_id, step_type, trans_project, departure, arrival, transport, fare_amount, trans_trip_type, trans_burden, trans_expense_total, trans_memo) " +
                     "VALUES (?, 'step3', ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tripAppId);
            ps.setString(2, d.getTransProject());
            ps.setString(3, d.getDeparture());
            ps.setString(4, d.getArrival());
            ps.setString(5, d.getTransport());
            ps.setString(6, d.getFareAmount());
            ps.setString(7, d.getTransTripType());
            ps.setString(8, d.getTransBurden());
            ps.setString(9, d.getTransExpenseTotal());
            ps.setString(10, d.getTransMemo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("Insert step3 detail failed");
    }
}