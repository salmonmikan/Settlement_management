package dao1;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Transportation;

public class TransportationDAO {

    public List<Transportation> getAll() {
        List<Transportation> list = new ArrayList<>();
        String sql = "SELECT * FROM transportation";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transportation item = new Transportation();
                item.setTransportationId(rs.getInt("transportation_id"));
                item.setRequestId(rs.getString("request_id"));
                item.setTransportationDate(rs.getDate("transportation_date").toLocalDate());
                item.setDepartureLocation(rs.getString("departure_location"));
                item.setArrivalLocation(rs.getString("arrival_location"));
                item.setCategory(rs.getString("category"));
                item.setTransportTypeId(rs.getString("transport_type_id"));
                item.setSettlementId(rs.getString("settlement_id"));
                item.setAbstractNote(rs.getString("abstract_note"));
                item.setPayer(rs.getString("payer"));
                item.setReceipt(rs.getString("receipt"));
                item.setAmount(rs.getInt("amount"));

                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
