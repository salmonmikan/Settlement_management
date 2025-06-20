package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Staff;
import util.DBConnection;

public class StaffDAO {
	// hash password
			public String hashPassword(String password) {
			    try {
			        MessageDigest md = MessageDigest.getInstance("SHA-256");
			        byte[] hash = md.digest(password.getBytes());
			        StringBuilder hexString = new StringBuilder();
			        for (byte b : hash) {
			            hexString.append(String.format("%02x", b));
			        }
			        return hexString.toString();
			    } catch (NoSuchAlgorithmException e) {
			        throw new RuntimeException(e);
			    }
			}
	
    public Staff findByIdAndPassword(String staffId, String rawPassword) {
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE staff_id = ? AND TRIM(password) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staffId);
//            stmt.setString(2, hashPassword(rawPassword));
            stmt.setString(2, rawPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                staff = new Staff();
                staff.setStaffId(rs.getString("staff_id"));
                staff.setName(rs.getString("name"));
                staff.setPosition(rs.getString("position")); 
                //by Son
                staff.setDepartment(rs.getString("department"));
                staff.setPassword(rs.getString("password"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return staff;
    }
}