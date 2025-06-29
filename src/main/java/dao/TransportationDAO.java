package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.TransportationApplicationBean;
import bean.TransportationDetailBean;
import bean.UploadedFile;
import util.DBConnection;

// ĐỔI TÊN CLASS NÀY CHO KHỚP VỚI SERVLET
public class TransportationDAO {

    /**
     * Chèn một record chi tiết đi lại vào bảng transportation_request.
     * (Code gốc của bạn, giữ nguyên)
     */
    public int insert(TransportationDetailBean detail, int applicationId, Connection conn) throws SQLException {
        String sql = "INSERT INTO transportation_request " +
                     "(application_id, project_code, date, departure_station, arrival_station, " +
                     "transport_type, amount, category, payer, abstract_note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, applicationId);
            stmt.setString(2, detail.getProjectCode());

            String dateStr = detail.getDate();
            if (dateStr != null && !dateStr.isBlank()) {
                try {
                    String formattedDate = dateStr.replace("/", "-");
                    stmt.setDate(3, java.sql.Date.valueOf(formattedDate));
                } catch (IllegalArgumentException e) {
                    throw new SQLException("Định dạng ngày tháng không hợp lệ (cần yyyy-MM-DD): " + dateStr, e);
                }
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            // Chú ý: trong DB là departure_station và arrival_station
            // nhưng bean của bạn lại là departure và arrival. Ta sẽ map chúng ở phần load.
            stmt.setString(4, detail.getDeparture());
            stmt.setString(5, detail.getArrival());
            stmt.setString(6, detail.getTransport());
            
            // Chú ý: trong DB là amount nhưng bean là fareAmount
            stmt.setInt(7, detail.getFareAmount());
            
            // Chú ý: trong DB là category và payer, nhưng bean là transTripType và burden
            stmt.setString(8, detail.getTransTripType());
            stmt.setString(9, detail.getBurden());
            
            // Chú ý: trong DB là abstract_note nhưng bean là transMemo
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

    /**
     * Tải toàn bộ chi tiết của một đơn 交通費 dựa vào application_id.
     * @param applicationId ID của đơn trong bảng applications
     * @return một đối tượng TransportationApplicationBean chứa tất cả thông tin
     * @throws SQLException
     */
    public TransportationApplicationBean loadByApplicationId(int applicationId) throws SQLException {
        TransportationApplicationBean appBean = new TransportationApplicationBean();
        List<TransportationDetailBean> details = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM transportation_request WHERE application_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();

            while (rs.next()) {
                TransportationDetailBean detail = new TransportationDetailBean();
                
                // Lấy khóa chính của dòng chi tiết này để tìm file
                int transportationId = rs.getInt("transportation_id");
                detail.setTransportationId(transportationId);
                
                // Nạp dữ liệu từ DB vào bean, chú ý các tên cột khác với tên thuộc tính
                detail.setApplicationId(rs.getInt("application_id"));
                detail.setProjectCode(rs.getString("project_code"));
                detail.setDate(rs.getDate("date").toString());
                detail.setDestination(rs.getString("destination")); // Giả định có cột destination trong DB
                detail.setDeparture(rs.getString("departure_station")); // DB: departure_station -> Bean: departure
                detail.setArrival(rs.getString("arrival_station"));     // DB: arrival_station -> Bean: arrival
                detail.setTransport(rs.getString("transport_type"));    // DB: transport_type -> Bean: transport
                detail.setFareAmount(rs.getInt("amount"));              // DB: amount -> Bean: fareAmount
                detail.setTransTripType(rs.getString("category"));      // DB: category -> Bean: transTripType
                detail.setBurden(rs.getString("payer"));                // DB: payer -> Bean: burden
                detail.setTransMemo(rs.getString("abstract_note"));     // DB: abstract_note -> Bean: transMemo
                
                // Tính toán expenseTotal (nếu cần, có thể business logic khác)
                detail.setExpenseTotal(rs.getInt("amount")); 

                // Tải các file đính kèm cho chi tiết này
                String fileSql = "SELECT original_file_name, stored_file_path FROM receipt_file WHERE block_id = ? AND block_type = 'transportation_request'";
                try (PreparedStatement psFile = conn.prepareStatement(fileSql)) {
                    psFile.setInt(1, transportationId);
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