// ✅ BusinessTripDAO.java: xử lý DB cho bảng business_trip liên quan
package dao;

import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DBConnection.getConnection;

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
 // === Load Step1Data ===
    public Step1Data loadStep1Data(int applicationId) throws Exception {
        String sql = "SELECT start_date, end_date, project_code, report, total_days FROM business_trip_application WHERE application_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Step1Data(
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("project_code"),
                        rs.getString("report"),
                        rs.getString("total_days")
                    );
                }
            }
        }
        return null;
    }
 // Xóa detail cũ trước khi insert lại (cho edit)
    public void deleteExistingTripDetails(int applicationId) throws Exception {
        String sql = "DELETE FROM business_trip_detail WHERE trip_application_id = " +
                     "(SELECT id FROM business_trip_application WHERE application_id = ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.executeUpdate();
        }
    }

    // === Load Step2 Detail list ===
    public List<Step2Detail> loadStep2List(int applicationId) throws Exception {
        List<Step2Detail> list = new ArrayList<>();
        String sql = "SELECT region_type, trip_type, hotel, burden, hotel_fee, daily_allowance, days, expense_total, memo " +
                     "FROM business_trip_detail WHERE trip_application_id = ? AND step_type = 'step2'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Step2Detail d = new Step2Detail(
                        rs.getString("region_type"),
                        rs.getString("trip_type"),
                        rs.getString("hotel"),
                        rs.getString("burden"),
                        rs.getString("hotel_fee"),
                        rs.getString("daily_allowance"),
                        rs.getString("days"),
                        rs.getString("expense_total"),
                        rs.getString("memo")
                    );
                    list.add(d);
                }
            }
        }
        return list;
    }

    // === Load Step3 Detail list ===
    public List<Step3Detail> loadStep3List(int applicationId) throws Exception {
        List<Step3Detail> list = new ArrayList<>();
        String sql = "SELECT trans_project, departure, arrival, transport, fare_amount, trans_trip_type, trans_burden, trans_expense_total, trans_memo " +
                     "FROM business_trip_detail WHERE trip_application_id = ? AND step_type = 'step3'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Step3Detail d = new Step3Detail(
                        rs.getString("trans_project"),
                        rs.getString("departure"),
                        rs.getString("arrival"),
                        rs.getString("transport"),
                        rs.getString("fare_amount"),
                        rs.getString("trans_trip_type"),
                        rs.getString("trans_burden"),
                        rs.getString("trans_expense_total"),
                        rs.getString("trans_memo")
                    );
                    list.add(d);
                }
            }
        }
        return list;
    }
    
    public BusinessTripBean loadBusinessTripByApplicationId(int applicationId) throws Exception {
        BusinessTripBean bean = new BusinessTripBean();

        // Step1
        String step1Sql = "SELECT * FROM business_trip_application WHERE application_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(step1Sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Step1Data s1 = new Step1Data(
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("project_code"),
                        rs.getString("report"),
                        rs.getString("total_days")
                    );
                    bean.setStep1Data(s1);
                }
            }
        }

        // Step2 + Step3
        String detailSql = "SELECT * FROM business_trip_detail WHERE trip_application_id = " +
                           "(SELECT id FROM business_trip_application WHERE application_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(detailSql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Step2Detail> s2List = new ArrayList<>();
                List<Step3Detail> s3List = new ArrayList<>();
                while (rs.next()) {
                    String stepType = rs.getString("step_type");
                    if ("step2".equals(stepType)) {
                        s2List.add(new Step2Detail(
                            rs.getString("region_type"),
                            rs.getString("trip_type"),
                            rs.getString("hotel"),
                            rs.getString("burden"),
                            rs.getString("hotel_fee"),
                            rs.getString("daily_allowance"),
                            rs.getString("days"),
                            rs.getString("expense_total"),
                            rs.getString("memo")
                        ));
                    } else if ("step3".equals(stepType)) {
                        s3List.add(new Step3Detail(
                            rs.getString("trans_project"),
                            rs.getString("departure"),
                            rs.getString("arrival"),
                            rs.getString("transport"),
                            rs.getString("fare_amount"),
                            rs.getString("trans_trip_type"),
                            rs.getString("trans_burden"),
                            rs.getString("trans_expense_total"),
                            rs.getString("trans_memo")
                        ));
                    }
                }
                bean.setStep2List(s2List);
                bean.setStep3List(s3List);
        }
        

        // Tổng tiền
        bean.setTotalStep2Amount(bean.getStep2List().stream().mapToInt(s -> Integer.parseInt(s.getExpenseTotal())).sum());
        bean.setTotalStep3Amount(bean.getStep3List().stream().mapToInt(s -> Integer.parseInt(s.getTransExpenseTotal())).sum());

        return bean;
    }
}
}
