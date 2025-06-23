package dao1;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.BusinessTripDetail;

public class BusinessTripDetailDAO {

    public List<BusinessTripDetail> getAll() {
        List<BusinessTripDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM businesstripdetail";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BusinessTripDetail item = new BusinessTripDetail();
                item.setLodgingId(rs.getInt("lodging_id"));
                item.setRequestId(rs.getString("request_id"));
                item.setCategoryId(rs.getString("category_id"));
                item.setTripStartDate(rs.getDate("trip_start_date").toLocalDate());
                item.setTripEndDate(rs.getDate("trip_end_date").toLocalDate());
                item.setNumberOfDays(rs.getInt("number_of_days"));
                item.setNightAllowance(rs.getInt("night_allowance"));
                item.setPayer(rs.getString("payer"));
                item.setReceipt(rs.getString("receipt"));

                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
