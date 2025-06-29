package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import bean.TransportationDetailBean;

//File: src/main/java/dao/TransportationDAO.java

//... các import khác ...

public class TransportationDAO {

 /**
  * Chèn một record chi tiết đi lại vào bảng transportation_request.
  * @param detail Chi tiết của một block
  * @param applicationId ID của đơn cha
  * @param conn Kết nối DB để xử lý transaction
  * @return ID của dòng vừa được chèn
  * @throws SQLException
  */
 public int insert(TransportationDetailBean detail, int applicationId, Connection conn) throws SQLException {
     String sql = "INSERT INTO transportation_request " +
                  "(application_id, project_code, date, departure_station, arrival_station, " +
                  "transport_type, amount, category, payer, abstract_note) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

     try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         stmt.setInt(1, applicationId);
         stmt.setString(2, detail.getProjectCode());

         // =================================================================
         // === BẮT ĐẦU KHỐI CODE SỬA LỖI ĐỊNH DẠNG NGÀY THÁNG ===
         // =================================================================
         String dateStr = detail.getDate();
         if (dateStr != null && !dateStr.isBlank()) {
             try {
                 // Chấp nhận cả định dạng YYYY/MM/DD bằng cách thay thế '/' bằng '-'
                 String formattedDate = dateStr.replace("/", "-");
                 stmt.setDate(3, java.sql.Date.valueOf(formattedDate));
             } catch (IllegalArgumentException e) {
                 // Nếu ngày tháng vẫn sai định dạng, ném ra một lỗi rõ ràng hơn
                 throw new SQLException("Định dạng ngày tháng không hợp lệ (cần YYYY-MM-DD): " + dateStr, e);
             }
         } else {
             // Nếu ngày tháng bị trống, đặt là NULL trong DB
             stmt.setNull(3, java.sql.Types.DATE);
         }
         // =================================================================
         // === KẾT THÚC KHỐI CODE SỬA LỖI ===
         // =================================================================

         stmt.setString(4, detail.getDeparture());
         stmt.setString(5, detail.getArrival());
         stmt.setString(6, detail.getTransport());
         stmt.setInt(7, detail.getFareAmount());
         stmt.setString(8, detail.getTransTripType());
         stmt.setString(9, detail.getBurden());
         stmt.setString(10, detail.getTransMemo());

         stmt.executeUpdate();

         try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
             if (generatedKeys.next()) {
                 return generatedKeys.getInt(1); // Trả về transportation_id
             } else {
                 throw new SQLException("Creating transportation detail failed, no ID obtained.");
             }
         }
     }
 }
}