package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import util.DBConnection;

public class ReceiptDAO {

    public int insertReceiptFile(String originalName, String storedPath) throws SQLException {
        String sql = "INSERT INTO receipt_file (original_file_name, stored_file_path) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, originalName);
            ps.setString(2, storedPath);
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public void insertReceiptMapping(int receiptId, String targetType, String targetId) throws SQLException {
        String sql = "INSERT INTO receipt_mapping (receipt_id, target_type, target_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, receiptId);
            ps.setString(2, targetType);
            ps.setString(3, targetId);
            ps.executeUpdate();
        }
    }

    /**
     * ✅ Thêm mới: insertMapping(Map receiptMap, applicationId)
     */
    public void insertMapping(Map<String, List<String>> receiptMap, int tripApplicationId) throws SQLException {
        if (receiptMap == null || receiptMap.isEmpty()) return;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            String insertFileSQL = "INSERT INTO receipt_file (original_file_name, stored_file_path) VALUES (?, ?)";
            String insertMapSQL = "INSERT INTO receipt_mapping (receipt_id, target_type, target_id) VALUES (?, ?, ?)";

            try (PreparedStatement fileStmt = conn.prepareStatement(insertFileSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement mapStmt = conn.prepareStatement(insertMapSQL)) {

                for (Map.Entry<String, List<String>> entry : receiptMap.entrySet()) {
                    String step = entry.getKey(); // step2 or step3
                    String targetType = step.equals("step2") ? "business_allowance_detail" : "business_transportation_detail";

                    for (String fileName : entry.getValue()) {
                        String originalName = fileName.substring(fileName.indexOf("_") + 1);
                        String storedPath = "uploads/" + fileName;

                        // Insert into receipt_file
                        fileStmt.setString(1, originalName);
                        fileStmt.setString(2, storedPath);
                        fileStmt.executeUpdate();

                        int receiptId = -1;
                        var rs = fileStmt.getGeneratedKeys();
                        if (rs.next()) {
                            receiptId = rs.getInt(1);
                        }

                        // Insert into receipt_mapping
                        mapStmt.setInt(1, receiptId);
                        mapStmt.setString(2, targetType);
                        mapStmt.setString(3, String.valueOf(tripApplicationId));
                        mapStmt.executeUpdate();
                    }
                }
            }

            conn.commit();
        }
    }
}