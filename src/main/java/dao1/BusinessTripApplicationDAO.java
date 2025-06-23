package dao1;

import static util.DBConnection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.BusinessTripApplication;

public class BusinessTripApplicationDAO {

    public List<BusinessTripApplication> getAll() {
        List<BusinessTripApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM businesstripapplication";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BusinessTripApplication item = new BusinessTripApplication();
                item.setRequestId(rs.getString("request_id"));
                item.setCategoryId(rs.getString("category_id"));
                item.setEmployeeId(rs.getString("employee_id"));
                item.setProjectCode(rs.getString("project_code"));
                item.setReport(rs.getString("report"));

                list.add(item);;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean insert(BusinessTripApplication item) {
        String sql = "INSERT INTO business_trip_application (tripRequestId, projectCode, employeeId, categoryId, report) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, item.getRequestId());
            stmt.setObject(3, item.getProjectCode());
            stmt.setObject(4, item.getEmployeeId());
            stmt.setObject(5, item.getCategoryId());
            stmt.setObject(6, item.getReport());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
