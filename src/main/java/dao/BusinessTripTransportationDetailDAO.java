package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import bean.Step3Detail;

public class BusinessTripTransportationDetailDAO {
    /**
     * 交通費明細をDBに挿入し、生成されたIDを返す。
     * (Chèn chi tiết chi phí di chuyển vào DB và trả về ID được tạo)
     */
    public int insert(Step3Detail detail, int tripApplicationId, Connection conn) throws SQLException {
        String sql = "INSERT INTO business_trip_transportation_detail (trip_application_id, trans_project, departure, arrival, transport, fare_amount, trans_trip_type, trans_burden, trans_expense_total, trans_memo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tripApplicationId);
            ps.setString(2, detail.getTransProject());
            ps.setString(3, detail.getDeparture());
            ps.setString(4, detail.getArrival());
            ps.setString(5, detail.getTransport());
            ps.setInt(6, detail.getFareAmount());
            ps.setString(7, detail.getTransTripType());
            ps.setString(8, detail.getTransBurden());
            ps.setInt(9, detail.getTransExpenseTotal());
            ps.setString(10, detail.getTransMemo());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                else throw new SQLException("交通費明細IDの取得に失敗しました。");
            }
        }
    }
}