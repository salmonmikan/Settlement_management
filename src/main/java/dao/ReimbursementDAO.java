package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.ReimbursementBean;
import util.DBConnection;

public class ReimbursementDAO {

    // Tạo ID tự động từ bảng sequence
    private String generateRequestId(Connection conn) throws SQLException {
        String sqlInsert = "INSERT INTO reimbursement_id_sequence VALUES ()";
        String sqlSelect = "SELECT LAST_INSERT_ID()";

        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("RE%06d", rs.getInt(1));
            }
        }
        return null;
    }

    // Hàm lưu đơn hoàn trả
    public String insertReimbursement(ReimbursementBean bean) {
        String sql = "INSERT INTO reimbursement_request " +
                     "(reimbursement_id, staff_id, project_code, date, destinations, accounting_item, amount, memo, abstract_note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String newId = generateRequestId(conn);

            stmt.setString(1, newId);
            stmt.setString(2, bean.getStaffId());
            stmt.setString(3, bean.getProjectCode());
            stmt.setDate(4, bean.getDate());
            stmt.setString(5, bean.getDestinations());
            stmt.setString(6, bean.getAccountingItem());
            stmt.setInt(7, bean.getAmount());
            stmt.setString(8, bean.getMemo());
            stmt.setString(9, bean.getAbstractNote());

            stmt.executeUpdate();

            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
