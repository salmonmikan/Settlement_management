
package dao;

import java.sql.*;

public class ApplicationHeaderDAO {
    private final Connection conn;

    public ApplicationHeaderDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertHeader(String staffId, String applicationType, int amount) throws SQLException {
        String sql = "INSERT INTO application_header (staff_id, application_type, application_date, amount, status) VALUES (?, ?, NOW(), ?, '提出済み')";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, staffId);
            ps.setString(2, applicationType);
            ps.setInt(3, amount);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Insert into application_header failed.");
    }

    public void updateHeader(int applicationId, int amount) throws SQLException {
        String sql = "UPDATE application_header SET amount = ?, status = '提出済み', application_date = NOW() WHERE application_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, applicationId);
            ps.executeUpdate();
        }
    }
}