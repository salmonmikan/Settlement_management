package dao;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.TransportationRequest;

public class TransportationRequestDAO {

    // Sinh mã ID tự động từ bảng tran_id_sequence
    private String generateRequestId(Connection conn) throws SQLException {
        String sqlInsert = "INSERT INTO tran_id_sequence VALUES ()";
        String sqlSelect = "SELECT LAST_INSERT_ID()";

        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("TR%06d", rs.getInt(1));  // VD: TR000001
            }
        }
        return null;
    }

    // Thêm mới đơn xin phương tiện
    public String insertRequest(TransportationRequest req) {
        String sql = "INSERT INTO transportation_request (transportation_request_id, staff_id, project_code, date, departure_station, arrival_station,amount, category, transport_type,payer, total_amount, abstract_note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = getConnection()) {
            String newId = generateRequestId(conn);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newId);
                ps.setString(2, req.getStaffId());
                ps.setString(3, req.getProjectCode());
                ps.setString(4, req.getDate());
                ps.setString(5, req.getDepartureStation());
                ps.setString(6, req.getArrivalStation());
                ps.setInt(7, req.getAmount());
                ps.setString(8, req.getCategory());
                ps.setString(9, req.getTransportType());
                ps.setString(10, req.getPayer());
                ps.setInt(11, req.getTotalAmount());
                ps.setString(12, req.getAbstractNote());

                ps.executeUpdate();
            }
            return newId;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy toàn bộ danh sách đơn phương tiện
    public List<TransportationRequest> getAllRequests() {
        List<TransportationRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM transportation_request ORDER BY date DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TransportationRequest req = new TransportationRequest();
                req.setTransportationRequestId(rs.getString("transportation_request_id"));
                req.setStaffId(rs.getString("staff_id"));
                req.setProjectCode(rs.getString("project_code"));
                req.setDate(rs.getString("date"));
                req.setDepartureStation(rs.getString("departure_station"));
                req.setArrivalStation(rs.getString("arrival_station"));
                req.setAmount(rs.getInt("amount"));
                req.setCategory(rs.getString("category"));
                req.setTransportType(rs.getString("transport_type"));
                req.setPayer(rs.getString("payer"));
                req.setTotalAmount(rs.getInt("totalAmount"));
                req.setAbstractNote(rs.getString("abstract_note"));
                list.add(req);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy chi tiết theo mã ID
    public TransportationRequest getRequestById(String staffId) {
        String sql = "SELECT * FROM transportation_request WHERE staff_id = ?";
        TransportationRequest req = null;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    req = new TransportationRequest();
                    req.setTransportationRequestId(rs.getString("transportation_request_id"));
                    req.setStaffId(rs.getString("staff_id"));
                    req.setProjectCode(rs.getString("project_code"));
                    req.setDate(rs.getString("date"));
                    req.setDepartureStation(rs.getString("departure_station"));
                    req.setArrivalStation(rs.getString("arrival_station"));
                    req.setAmount(rs.getInt("amount"));
                    req.setCategory(rs.getString("category"));
                    req.setTransportType(rs.getString("transport_type"));
                    req.setTotalAmount(rs.getInt("totalAmount"));
                    req.setAbstractNote(rs.getString("abstract_note"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return req;
    }
}