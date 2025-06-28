package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import bean.Step1Data;

public class BusinessTripApplicationDAO {
    /**
     * 出張申請の基本情報をDBに挿入し、生成されたIDを返す。
     * (Chèn thông tin cơ bản của chuyến đi vào DB và trả về ID được tạo)
     */
	public int insert(Step1Data step1Data, int applicationId, Connection conn) throws SQLException {
	    String sql = "INSERT INTO business_trip_application (application_id, start_date, end_date, project_code, report, total_days) VALUES (?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        ps.setInt(1, applicationId);
	        
	        // Thay thế '/' bằng '-' trước khi gọi valueOf
	        ps.setDate(2, Date.valueOf(step1Data.getStartDate().replace('/', '-')));
	        ps.setDate(3, Date.valueOf(step1Data.getEndDate().replace('/', '-')));

	        ps.setString(4, step1Data.getProjectCode());
	        ps.setString(5, step1Data.getTripReport());
	        ps.setInt(6, step1Data.getTotalDays());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("出張申請の作成に失敗しました。");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("出張申請IDの取得に失敗しました。");
                }
            }
        }
    }
}