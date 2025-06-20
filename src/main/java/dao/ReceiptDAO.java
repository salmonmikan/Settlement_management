package dao;

import java.sql.*;
import static util.DBConnection.getConnection;

public class ReceiptDAO {

    /**
     * Lưu thông tin file đính kèm (bill) vào bảng receipt_file.
     */
    public void insertReceipt(String refTable, int refId, String originalName, String storedPath) throws Exception {
        String sql = "INSERT INTO receipt_file (ref_table, ref_id, original_file_name, stored_file_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, refTable);
            ps.setInt(2, refId);
            ps.setString(3, originalName);
            ps.setString(4, storedPath);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("【ReceiptDAO】Lỗi khi insert bill file:");
            System.err.println("refTable=" + refTable + ", refId=" + refId + ", original=" + originalName);
            throw e;
        }
    }
}