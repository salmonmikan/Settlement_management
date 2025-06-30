package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ApplicationHeaderDAO {
    /**
     * 申請ヘッダー情報をDBに挿入し、生成されたIDを返す。
     * (Chèn thông tin header của đơn vào DB và trả về ID được tạo)
     */
    public int insert(int totalAmount, String staffId, Connection conn) throws SQLException {
        String sql = "INSERT INTO application_header (staff_id, application_type, amount, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, staffId);
            ps.setString(2, "出張費"); // Loại đơn mặc định
            ps.setInt(3, totalAmount);
            ps.setString(4, "提出済み"); // Trạng thái mặc định

            if (ps.executeUpdate() == 0) {
                throw new SQLException("ヘッダーの作成に失敗しました。");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("ヘッダーIDの取得に失敗しました。");
                }
            }
        }
    }
}