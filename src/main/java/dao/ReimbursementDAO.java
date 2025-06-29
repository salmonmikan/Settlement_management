package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.ReimbursementApplicationBean;
import bean.ReimbursementDetailBean;
import bean.UploadedFile;
import util.DBConnection;

public class ReimbursementDAO {

    /**
     * Chèn một record vào bảng reimbursement_request
     * (Code gốc của bạn, giữ nguyên)
     */
    public int insert(ReimbursementDetailBean detail, int applicationId, Connection conn) throws SQLException {
        String sql = "INSERT INTO reimbursement_request " +
                     "(application_id, project_code, date, destinations, accounting_item, amount, report) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, applicationId);
            stmt.setString(2, detail.getProjectCode());

            String dateStr = detail.getDate();
            if (dateStr != null && !dateStr.isBlank()) {
                try {
                    dateStr = dateStr.replace("/", "-");
                    stmt.setDate(3, java.sql.Date.valueOf(dateStr));
                } catch (IllegalArgumentException e) {
                    throw new SQLException("日付の形式が不正です（形式: Findlay-MM-DD）: " + dateStr, e);
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

    /**
     * === PHƯƠNG THỨC MỚI ĐƯỢC THÊM VÀO ===
     * Tải toàn bộ chi tiết của một đơn 立替金 dựa vào application_id.
     * @param applicationId ID của đơn trong bảng applications
     * @return một đối tượng ReimbursementApplicationBean chứa tất cả thông tin
     * @throws SQLException
     */
    public ReimbursementApplicationBean loadByApplicationId(int applicationId) throws SQLException {
        ReimbursementApplicationBean appBean = new ReimbursementApplicationBean();
        List<ReimbursementDetailBean> details = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM reimbursement_request WHERE application_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ReimbursementDetailBean detail = new ReimbursementDetailBean();
                
                // Lấy khóa chính của dòng chi tiết này để tìm file đính kèm
                int reimbursementId = rs.getInt("reimbursement_id");
                
                // Nạp dữ liệu từ DB vào bean
                detail.setProjectCode(rs.getString("project_code"));
                if (rs.getDate("date") != null) {
                    detail.setDate(rs.getDate("date").toString());
                }
                detail.setDestinations(rs.getString("destinations"));
                detail.setAccountingItem(rs.getString("accounting_item"));
                detail.setAmount(rs.getInt("amount"));
                detail.setReport(rs.getString("report"));

                // Tải các file đính kèm cho chi tiết này
                String fileSql = "SELECT original_file_name, stored_file_path FROM receipt_file WHERE block_id = ? AND block_type = 'reimbursement_request'";
                try (PreparedStatement psFile = conn.prepareStatement(fileSql)) {
                    psFile.setInt(1, reimbursementId);
                    try (ResultSet rsFile = psFile.executeQuery()) {
                        List<UploadedFile> files = new ArrayList<>();
                        while (rsFile.next()) {
                            UploadedFile file = new UploadedFile();
                            file.setOriginalFileName(rsFile.getString("original_file_name"));
                            file.setTemporaryPath(rsFile.getString("stored_file_path"));
                            files.add(file);
                        }
                        detail.setTemporaryFiles(files);
                    }
                }
                
                details.add(detail);
            }
            
            appBean.setDetails(details);
            appBean.calculateTotalAmount(); // Tính tổng tiền

        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        
        return appBean;
    }
}