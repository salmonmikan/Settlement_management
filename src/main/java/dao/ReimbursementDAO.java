package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import bean.ReimbursementApplicationBean;
import bean.ReimbursementDetailBean;
import bean.UploadedFile;
import util.DBConnection;

public class ReimbursementDAO {

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
                    String normalizedDateStr = dateStr.replace('/', '-');
                    LocalDate localDate;
                    try {
                        localDate = LocalDate.parse(normalizedDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                    } catch (DateTimeParseException e) {
                        DateTimeFormatter twoDigitYearFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");
                        localDate = LocalDate.parse(normalizedDateStr, twoDigitYearFormatter);
                    }
                    stmt.setDate(3, java.sql.Date.valueOf(localDate));
                } catch (DateTimeParseException e) {
                    throw new SQLException("Định dạng ngày tháng không hợp lệ (cần yyyy-MM-dd hoặc yy-MM-dd): " + dateStr, e);
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
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating reimbursement detail failed, no ID obtained.");
                }
            }
        }
    }

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
                int reimbursementId = rs.getInt("reimbursement_id");
                
                detail.setProjectCode(rs.getString("project_code"));
                if (rs.getDate("date") != null) {
                    detail.setDate(rs.getDate("date").toString());
                }
                detail.setDestinations(rs.getString("destinations"));
                detail.setAccountingItem(rs.getString("accounting_item"));
                detail.setAmount(rs.getInt("amount"));
                
                // === SỬA LỖI Ở ĐÂY ===
                // Đọc từ cột "report" thay vì "abstract_note"
                detail.setReport(rs.getString("report"));

                String fileSql = "SELECT original_file_name, stored_file_path FROM receipt_file WHERE block_id = ? AND block_type = 'reimbursement_request'";
                try (PreparedStatement psFile = conn.prepareStatement(fileSql)) {
                    psFile.setInt(1, reimbursementId);
                    try (ResultSet rsFile = psFile.executeQuery()) {
                        List<UploadedFile> files = new ArrayList<>();
                        while (rsFile.next()) {
                            UploadedFile file = new UploadedFile();
                            file.setOriginalFileName(rsFile.getString("original_file_name"));
                            String storedPath = rsFile.getString("stored_file_path");
                            file.setTemporaryPath(storedPath);
                            if (storedPath != null && storedPath.contains("/")) {
                                file.setUniqueStoredName(storedPath.substring(storedPath.lastIndexOf('/') + 1));
                            }
                            files.add(file);
                        }
                        detail.setTemporaryFiles(files);
                    }
                }
                details.add(detail);
            }
            appBean.setDetails(details);
            appBean.calculateTotalAmount();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return appBean;
    }

    public void deleteByApplicationId(int applicationId, Connection conn) throws SQLException {
        String sql = "DELETE FROM reimbursement_request WHERE application_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.executeUpdate();
        }
    }
}