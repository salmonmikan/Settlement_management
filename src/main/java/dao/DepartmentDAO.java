package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Staff;
import util.DBConnection;

public class DepartmentDAO {
    public Staff findByIdAndPassword(String staffId, String password) {
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE staff_id = ? AND TRIM(password) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staffId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                staff = new Staff();
                staff.setStaffId(rs.getString("staff_id"));
                staff.setName(rs.getString("name"));
                staff.setPosition(rs.getString("position")); // ✨
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return staff;
    }
}