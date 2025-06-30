package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.PaymentBean;
import util.DBConnection;

public class PaymentDAO {

    public void updateStatusToPaid(int applicationId) {
        String sql = "UPDATE application_header SET status = '支払済み' WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PaymentBean> findAll() {
        ArrayList<PaymentBean> list = new ArrayList<>();

        String sql = "SELECT a.application_id, a.staff_id, s.name AS staff_name, " +
                     "a.application_type, a.created_at, a.amount, a.status " +
                     "FROM application_header a " +
                     "LEFT JOIN staff s ON a.staff_id = s.staff_id " +
                     "WHERE a.delete_flag = 0 " +
                     "ORDER BY a.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PaymentBean bean = new PaymentBean();
                bean.setApplicationId(rs.getInt("application_id"));
                bean.setStaffId(rs.getString("staff_id"));
                bean.setStaffName(rs.getString("staff_name"));
                bean.setApplicationType(rs.getString("application_type"));
                bean.setCreatedAt(rs.getString("created_at")); // ← Sửa tên hàm & cột ở đây
                bean.setAmount(rs.getInt("amount"));
                bean.setStatus(rs.getString("status"));
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}