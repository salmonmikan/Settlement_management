package dao;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequestIdGeneratorDAO {

    public String generateTripRequestId() {
        String tripId = null;

        String sql = "INSERT INTO trip_id_sequence () VALUES ()";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                int seq = rs.getInt(1);
                tripId = String.format("BT%08d", seq);  // VD: BT00000023
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tripId;
    }
}