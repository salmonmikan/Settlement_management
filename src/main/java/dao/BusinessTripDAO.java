// ✅ Refactored BusinessTripDAO.java with RETURN_GENERATED_KEYS
package dao;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;

public class BusinessTripDAO {

    public int insertApplicationHeader(int staffId, String applicationType, String applicationDate, int amount, String status) throws Exception {
        String sql = "INSERT INTO application_header (staff_id, application_type, application_date, amount, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, String.valueOf(staffId));
            ps.setString(2, applicationType);
            ps.setString(3, applicationDate);
            ps.setInt(4, amount);
            ps.setString(5, status);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("Insert application_header failed");
    }

    public int insertBusinessTripApplication(int applicationId, Step1Data s1) throws Exception {
        String sql = "INSERT INTO business_trip_application (application_id, start_date, end_date, project_code, report, total_days) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, applicationId);
            ps.setString(2, s1.getStartDate());
            ps.setString(3, s1.getEndDate());
            ps.setString(4, s1.getProjectCode());
            ps.setString(5, s1.getReport());
            ps.setInt(6, Integer.parseInt(s1.getTotalDays()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("Insert business_trip_application failed");
    }

    public int insertAllowanceDetail(int tripAppId, Step2Detail d) throws Exception {
        String sql = "INSERT INTO allowance_detail (trip_application_id, region_type, trip_type, hotel, burden, hotel_fee, daily_allowance, days, expense_total, memo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tripAppId);
            ps.setString(2, d.getRegionType());
            ps.setString(3, d.getTripType());
            ps.setString(4, d.getHotel());
            ps.setString(5, d.getBurden());
            ps.setInt(6, Integer.parseInt(d.getHotelFee()));
            ps.setInt(7, Integer.parseInt(d.getDailyAllowance()));
            ps.setInt(8, Integer.parseInt(d.getDays()));
            ps.setInt(9, Integer.parseInt(d.getExpenseTotal()));
            ps.setString(10, d.getMemo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("Insert allowance_detail failed");
    }

    public int insertTransportDetail(int tripAppId, Step3Detail d) throws Exception {
        String sql = "INSERT INTO business_trip_transportation_detail (trip_application_id, trans_project, departure, arrival, transport, fare_amount, trans_trip_type, trans_burden, trans_expense_total, trans_memo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tripAppId);
            ps.setString(2, d.getTransProject());
            ps.setString(3, d.getDeparture());
            ps.setString(4, d.getArrival());
            ps.setString(5, d.getTransport());
            ps.setInt(6, Integer.parseInt(d.getFareAmount()));
            ps.setString(7, d.getTransTripType());
            ps.setString(8, d.getTransBurden());
            ps.setInt(9, Integer.parseInt(d.getTransExpenseTotal()));
            ps.setString(10, d.getTransMemo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("Insert business_trip_transportation_detail failed");
    }

    public void deleteAllowanceDetails(int tripAppId) throws Exception {
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM allowance_detail WHERE trip_application_id = ?")) {
            ps.setInt(1, tripAppId);
            ps.executeUpdate();
        }
    }

    public void deleteTransportDetails(int tripAppId) throws Exception {
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM business_trip_transportation_detail WHERE trip_application_id = ?")) {
            ps.setInt(1, tripAppId);
            ps.executeUpdate();
        }
    }

    public List<Step2Detail> loadStep2List(int tripAppId) throws Exception {
        List<Step2Detail> list = new ArrayList<>();
        String sql = "SELECT region_type, trip_type, hotel, burden, hotel_fee, daily_allowance, days, expense_total, memo FROM allowance_detail WHERE trip_application_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripAppId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Step2Detail(
                        rs.getString("region_type"),
                        rs.getString("trip_type"),
                        rs.getString("hotel"),
                        rs.getString("burden"),
                        String.valueOf(rs.getInt("hotel_fee")),
                        String.valueOf(rs.getInt("daily_allowance")),
                        String.valueOf(rs.getInt("days")),
                        String.valueOf(rs.getInt("expense_total")),
                        rs.getString("memo")
                    ));
                }
            }
        }
        return list;
    }

    public List<Step3Detail> loadStep3List(int tripAppId) throws Exception {
        List<Step3Detail> list = new ArrayList<>();
        String sql = "SELECT trans_project, departure, arrival, transport, fare_amount, trans_trip_type, trans_burden, trans_expense_total, trans_memo FROM business_trip_transportation_detail WHERE trip_application_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tripAppId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Step3Detail(
                        rs.getString("trans_project"),
                        rs.getString("departure"),
                        rs.getString("arrival"),
                        rs.getString("transport"),
                        String.valueOf(rs.getInt("fare_amount")),
                        rs.getString("trans_trip_type"),
                        rs.getString("trans_burden"),
                        String.valueOf(rs.getInt("trans_expense_total")),
                        rs.getString("trans_memo")
                    ));
                }
            }
        }
        return list;
    }

    public BusinessTripBean loadBusinessTripByApplicationId(int applicationId) throws Exception {
        BusinessTripBean bean = new BusinessTripBean();

        // Step1
        String sql1 = "SELECT * FROM business_trip_application WHERE application_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql1)) {
            ps.setInt(1, applicationId);//
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Step1Data s1 = new Step1Data(
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("project_code"),
                        rs.getString("report"),
                        String.valueOf(rs.getInt("total_days"))
                    );
                    bean.setStep1Data(s1);
                    int tripAppId = rs.getInt("trip_application_id");
                    bean.setStep2List(loadStep2List(tripAppId));
                    bean.setStep3List(loadStep3List(tripAppId));
                    bean.setTotalStep2Amount(bean.getStep2List().stream().mapToInt(s -> Integer.parseInt(s.getExpenseTotal())).sum());
                    bean.setTotalStep3Amount(bean.getStep3List().stream().mapToInt(s -> Integer.parseInt(s.getTransExpenseTotal())).sum());
                }
            }
        }
        return bean;
    }
}