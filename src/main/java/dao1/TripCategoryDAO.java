package dao1;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.TripCategory;

public class TripCategoryDAO {

    public List<TripCategory> getAll() {
        List<TripCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM trip_category";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

        	while (rs.next()) {
        	    TripCategory item = new TripCategory();
        	    item.setCategoryId(rs.getString("category_id"));
        	    item.setTripType(rs.getString("trip_type"));
        	    item.setRegionType(rs.getString("region_type"));
        	    item.setHotelFee(rs.getInt("hotel_fee"));
        	    item.setDailyAllowance(rs.getInt("daily_allowance"));
        	    list.add(item);
        	}

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
