
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import bean.UploadedFile;

public class ReceiptDAO {

    /**
     * PHƯƠNG THỨC INSERT DUY NHẤT (7 tham số).
     * Phiên bản này khớp hoàn toàn với cấu trúc bảng `receipt_file` của bạn
     * và không chứa cột 'ref_id' không tồn tại.
     */
    public void insert(int applicationId, String blockType, int blockId, int receiptIndex, UploadedFile file, String staffId, Connection conn) throws SQLException {
        String sql = "INSERT INTO receipt_file (application_id, block_type, block_id, receipt_index, original_file_name, stored_file_path, uploaded_by, uploaded_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.setString(2, blockType);
            ps.setInt(3, blockId);
            ps.setInt(4, receiptIndex);
            ps.setString(5, file.getOriginalFileName());
            ps.setString(6, file.getTemporaryPath()); 
            ps.setString(7, staffId);

            ps.executeUpdate();
        }
    }

    /**
     * Xóa các file dựa trên application_id. Dùng khi xóa toàn bộ đơn.
     */
    public void deleteByApplicationId(int applicationId, Connection conn) throws SQLException {
        String sql = "DELETE FROM receipt_file WHERE application_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.executeUpdate();
        }
    }
    
    /**
     * Xóa các file dựa trên application_id VÀ loại block.
     * Dùng trong logic Update "Xóa-rồi-Ghi-lại" cho các đơn không phải Business Trip.
     */
    public void deleteByApplicationIdAndBlockType(int applicationId, String blockType, Connection conn) throws SQLException {
        String sql = "DELETE FROM receipt_file WHERE application_id = ? AND block_type = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.setString(2, blockType);
            ps.executeUpdate();
        }
    }
}