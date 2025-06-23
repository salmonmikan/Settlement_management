package dao;

import java.sql.*;
import java.util.*;

import static util.DBConnection.getConnection;

public class ReceiptDAO {

    /**
     * Lưu thông tin file đính kèm vào bảng receipt_file (bao gồm application_id và application_type).
     */
    public void insertReceipt(String refTable, int refId, String originalName, String storedPath, int applicationId, String applicationType) throws Exception {
        String sql = "INSERT INTO receipt_file (ref_table, ref_id, original_file_name, stored_file_path, application_id, application_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, refTable);
            ps.setInt(2, refId);
            ps.setString(3, originalName);
            ps.setString(4, storedPath);
            ps.setInt(5, applicationId);
            ps.setString(6, applicationType);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("【ReceiptDAO】Lỗi khi insert file:");
            System.err.println("refTable=" + refTable + ", refId=" + refId + ", original=" + originalName + ", appId=" + applicationId + ", type=" + applicationType);
            throw e;
        }
    }

    /**
     * Lấy danh sách file đính kèm theo applicationId và phân loại theo step2 / step3.
     */
    public Map<String, List<String>> getReceiptsByApplicationId(int applicationId) {
        Map<String, List<String>> resultMap = new HashMap<>();
        String sql = "SELECT application_type, stored_file_path FROM receipt_file WHERE application_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("application_type");  // "step2" hoặc "step3"
                    String file = rs.getString("stored_file_path");
                    resultMap.computeIfAbsent(type, k -> new ArrayList<>()).add(file);
                }
            }

        } catch (SQLException e) {
            System.err.println("【ReceiptDAO】Lỗi khi lấy file theo applicationId: " + applicationId);
            e.printStackTrace();
        }

        return resultMap;
    }
}