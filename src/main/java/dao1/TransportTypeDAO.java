package dao1;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.TransportType;

public class TransportTypeDAO {

    public List<TransportType> getAll() {
        List<TransportType> list = new ArrayList<>();
        String sql = "SELECT * FROM transport_type";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TransportType item = new TransportType();
                item.setTransportTypeId(rs.getString("transport_type_id"));
                item.setTransportTypeName(rs.getString("transport_type_name"));

                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
