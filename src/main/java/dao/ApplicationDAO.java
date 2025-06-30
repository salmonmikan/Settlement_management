package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Application;
import util.DBConnection;

public class ApplicationDAO {

    public int insertApplication(String type, String staffId, int amount, Connection conn) throws SQLException {
        String sql = "INSERT INTO application_header (application_type, staff_id, status, amount, created_at) " +
                     "VALUES (?, ?, '未提出', ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, type);
            ps.setString(2, staffId);
            ps.setInt(3, amount);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("application_header の登録に失敗しました。");
    }

    public void setApprover(int applicationId, String approverId, Connection conn) throws SQLException {
        String sql = "UPDATE application_header SET approver_id = ? WHERE application_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, approverId);
            ps.setInt(2, applicationId);
            ps.executeUpdate();
        }
    }

    public void submitApplicationIfNotYet(int applicationId, String staffId, String approverId, Connection conn) throws SQLException {
        String sql = "UPDATE application_header SET status = '提出済み', approver_id = ? " +
                     "WHERE application_id = ? AND staff_id = ? AND status = '未提出'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, approverId);
            ps.setInt(2, applicationId);
            ps.setString(3, staffId);
            ps.executeUpdate();
        }
    }

    public List<Application> getAllApplications() throws Exception {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT application_id, application_type, created_at, amount, status FROM application_header ORDER BY application_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Application a = new Application();
                a.setApplicationId(rs.getInt("application_id"));
                a.setApplicationType(rs.getString("application_type"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setAmount(rs.getInt("amount"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        }
        return list;
    }

    public List<Application> getApplicationsByStaffId(String staffId) throws Exception {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT application_id, application_type, created_at, amount, status FROM application_header WHERE staff_id = ? ORDER BY application_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application a = new Application();
                    a.setApplicationId(rs.getInt("application_id"));
                    a.setApplicationType(rs.getString("application_type"));
                    a.setCreatedAt(rs.getTimestamp("created_at"));
                    a.setAmount(rs.getInt("amount"));
                    a.setStatus(rs.getString("status"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    public String findManagerId(String staffId) throws Exception {
        String sql = """
            SELECT s2.staff_id
            FROM staff s1
            JOIN staff s2 ON s1.department_id = s2.department_id
            JOIN position_master p2 ON s2.position_id = p2.position_id
            WHERE s1.staff_id = ?
              AND p2.position_name = '部長'
              AND s2.delete_flag = 0 AND p2.delete_flag = 0
            LIMIT 1
        """;
        try (Connection conn = DBConnection.getConnection();
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
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("status");
            }
        }
        return null;
    }

    public String getApplicationTypeById(int applicationId) throws Exception {
        String sql = "SELECT application_type FROM application_header WHERE application_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("application_type");
            }
        }
        return null;
    }

    public void updateApplicationAmount(int applicationId, int newAmount) throws Exception {
        String sql = "UPDATE application_header SET amount = ? WHERE application_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newAmount);
            ps.setInt(2, applicationId);
            ps.executeUpdate();
        }
    }

    public List<Application> getApplicationsByApprover(String approverId) throws Exception {
        List<Application> list = new ArrayList<>();
        String sql = """
            SELECT 
                ah.application_id, ah.application_type, ah.created_at,
                ah.amount, ah.status, s.staff_id, s.name
            FROM application_header ah
            JOIN staff s ON ah.staff_id = s.staff_id
            WHERE ah.approver_id = ? AND ah.delete_flag = 0
            ORDER BY ah.created_at DESC
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, approverId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application a = new Application();
                    a.setApplicationId(rs.getInt("application_id"));
                    a.setApplicationType(rs.getString("application_type"));
                    a.setCreatedAt(rs.getTimestamp("created_at"));
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

    public void updateApplication(int applicationId, int totalAmount, Connection conn) throws SQLException {
        String sql = "UPDATE application_header SET amount = ?, updated_at = NOW() WHERE application_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, totalAmount);
            ps.setInt(2, applicationId);
            ps.executeUpdate();
        }
    }
}