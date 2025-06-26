package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnection;

public class PassDao {
    // パスワード確認（プレーンテキスト比較）
    public boolean checkPassword(String staffId, String inputPassword) {
        boolean isValid = false;

        String sql = "SELECT password FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staffId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                isValid = dbPassword.equals(inputPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    
    // パスワード更新
    public boolean updatePassword(String staffId, String newPassword) {
        String sql = "UPDATE staff SET password = ? WHERE staff_id = ?";
        boolean isUpdated = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, staffId);

            int rows = stmt.executeUpdate();
            isUpdated = (rows == 1); // 更新成功は1行のみのはず

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }
}



    
    