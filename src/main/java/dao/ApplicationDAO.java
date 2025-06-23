package dao;

import model.Application;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DBConnection.getConnection;

public class ApplicationDAO {

    public int insertApplication(String type, String staffId, int amount) throws Exception {
        String sql = "INSERT INTO application_header (application_type, staff_id, status, amount, application_date, created_at) " +
                     "VALUES (?, ?, '未提出', ?, NOW(), NOW())";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, type);
            ps.setString(2, staffId);
            ps.setInt(3, amount);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new Exception("application_header の登録に失敗しました。");
    }

    public List<Application> getAllApplications() throws Exception {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT application_id, application_type, application_date, amount, status FROM application_header ORDER BY application_id DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Application a = new Application();
                a.setApplicationId(rs.getInt("application_id"));
                a.setApplicationType(rs.getString("application_type"));
                a.setApplicationDate(rs.getTimestamp("application_date")); // ✅ Đúng
                a.setAmount(rs.getInt("amount"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        }
        return list;
    }

    public List<Application> getApplicationsByStaffId(String staffId) throws Exception {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT application_id, application_type, application_date, amount, status FROM application_header WHERE staff_id = ? ORDER BY application_id DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application a = new Application();
                    a.setApplicationId(rs.getInt("application_id"));
                    a.setApplicationType(rs.getString("application_type"));
                    a.setApplicationDate(rs.getTimestamp("application_date")); // ✅ Đúng
                    a.setAmount(rs.getInt("amount"));
                    a.setStatus(rs.getString("status"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    public void submitApplicationIfNotYet(int applicationId, String staffId) throws Exception {
        String sql = "UPDATE application_header SET status = '提出済み' " +
                     "WHERE application_id = ? AND staff_id = ? AND status = '未提出'";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.setString(2, staffId);
            ps.executeUpdate();
        }
    }

    public String findManagerId(String staffId) throws Exception {
        String sql = """
            SELECT s2.staff_id
            FROM staff s1
            JOIN staff s2 ON s1.department = s2.department
            WHERE s1.staff_id = ? AND s2.position = '部長'
            """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("staff_id");
                }
            }
        }
        return null;
    }

    public String getApplicationStatus(int appId) throws Exception {
        String sql = "SELECT status FROM application_header WHERE application_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("status");
            }
        }
        return null;
    }

    public String getStaffPosition(String staffId) throws Exception {
        String sql = "SELECT position FROM staff WHERE staff_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("position");
            }
        }
        return null;
    }

    public String getApplicationTypeById(int applicationId) throws Exception {
        String sql = "SELECT application_type FROM application_header WHERE application_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("application_type");
            }
        }
        return null;
    }

    // 🔧 Sửa lỗi column name
    public void updateApplicationAmount(int applicationId, int newAmount) throws Exception {
        String sql = "UPDATE application_header SET amount = ? WHERE application_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newAmount);
            ps.setInt(2, applicationId);
            ps.executeUpdate();
        }
    }

    public List<Application> getSubmittedApplicationsByDepartment(String department) throws Exception {
        List<Application> list = new ArrayList<>();
        String sql = """
            SELECT ah.application_id, ah.application_type, ah.application_date,
                   ah.amount, ah.status, s.staff_id, s.name
            FROM application_header ah
            JOIN staff s ON ah.staff_id = s.staff_id
            WHERE s.department = ? AND ah.status = '提出済み'
            ORDER BY ah.application_date DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, department);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application a = new Application();
                    a.setApplicationId(rs.getInt("application_id"));
                    a.setApplicationType(rs.getString("application_type"));
                    a.setApplicationDate(rs.getTimestamp("application_date")); // ✅ Đúng
                    a.setAmount(rs.getInt("amount"));
                    a.setStatus(rs.getString("status"));
                    a.setStaffId(rs.getString("staff_id"));
                    a.setStaffName(rs.getString("name"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    // ✅ Thêm hàm còn thiếu để lấy chi tiết application theo ID
    public Application findApplicationById(int applicationId) throws Exception {
        String sql = "SELECT * FROM application_header WHERE application_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, applicationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Application app = new Application();
                    app.setApplicationId(rs.getInt("application_id"));
                    app.setStaffId(rs.getString("staff_id"));
                    app.setApplicationType(rs.getString("application_type"));
                    app.setApplicationDate(rs.getTimestamp("application_date")); // ✅ Đúng
                    app.setAmount(rs.getInt("amount"));
                    app.setStatus(rs.getString("status"));
                    return app;
                }
            }
        }
        return null;
    }
}