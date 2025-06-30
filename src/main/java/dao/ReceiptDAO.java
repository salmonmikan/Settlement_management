package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import bean.UploadedFile;

public class ReceiptDAO {
    /**
     * Chèn một record file hóa đơn vào DB.
     * @param applicationId ID của đơn đăng ký chính
     * @param blockType Loại block ('allowance_detail' hoặc 'business_trip_transportation_detail')
     * @param blockId ID của dòng chi tiết (ví dụ: ID từ bảng allowance_detail)
     * @param receiptIndex CHỈ SỐ của file này bên trong block (0, 1, 2, ...)
     * @param file Đối tượng file chứa thông tin tên, đường dẫn
     * @param staffId ID của người upload
     * @param conn Connection đến DB (để chạy trong transaction)
     * @throws SQLException
     */
    public void insert(int applicationId, String blockType, int blockId, int receiptIndex, UploadedFile file, String staffId, Connection conn) throws SQLException {
        String sql = "INSERT INTO receipt_file (application_id, block_type, block_id, receipt_index, original_file_name, stored_file_path, uploaded_by, uploaded_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.setString(2, blockType);
            ps.setInt(3, blockId);
            ps.setInt(4, receiptIndex); // Sửa ở đây: Lấy từ tham số truyền vào
            ps.setString(5, file.getOriginalFileName());
            
            // Giả sử file.getTemporaryPath() là đường dẫn cuối cùng
            // Nếu bạn cần chuyển file, logic đó sẽ nằm trong service
            ps.setString(6, file.getTemporaryPath()); 
            
            ps.setString(7, staffId);

            ps.executeUpdate();
        }
    }
    public void deleteByApplicationId(int applicationId, Connection conn) throws SQLException {
        String sql = "DELETE FROM receipt_file WHERE application_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.executeUpdate();
        }
    }
}