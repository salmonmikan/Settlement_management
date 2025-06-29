package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bean.ReimbursementDetailBean;

public class ReimbursementDAO {

    /**
     * Chèn một record vào bảng reimbursement_request
     * @param detail         Chi tiết từng block reimbursement
     * @param applicationId  ID của đơn cha trong bảng application_header
     * @param conn           Kết nối DB (truyền từ ngoài để xử lý transaction)
     * @return               reimbursement_id vừa được insert
     * @throws SQLException
     */
    public int insert(ReimbursementDetailBean detail, int applicationId, Connection conn) throws SQLException {
        String sql = "INSERT INTO reimbursement_request " +
                     "(application_id, project_code, date, destinations, accounting_item, amount, report) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, applicationId);
            stmt.setString(2, detail.getProjectCode());

            // ✅ FORMAT NGÀY: xử lý cả trường hợp YYYY/MM/DD hoặc YYYY-MM-DD
            String dateStr = detail.getDate();
            if (dateStr != null && !dateStr.isBlank()) {
                try {
                    // Chuyển '/' thành '-' để tránh lỗi IllegalArgumentException
                    dateStr = dateStr.replace("/", "-");
                    stmt.setDate(3, java.sql.Date.valueOf(dateStr)); // Chấp nhận định dạng YYYY-MM-DD
                } catch (IllegalArgumentException e) {
                    throw new SQLException("日付の形式が不正です（形式: YYYY-MM-DD）: " + dateStr, e);
                }
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            stmt.setString(4, detail.getDestinations());
            stmt.setString(5, detail.getAccountingItem());
            stmt.setInt(6, detail.getAmount());
            stmt.setString(7, detail.getReport());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // reimbursement_id
                } else {
                    throw new SQLException("Creating reimbursement detail failed, no ID obtained.");
                }
            }
        }
    }
}