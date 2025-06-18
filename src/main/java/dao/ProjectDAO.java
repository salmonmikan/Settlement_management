package dao;

import java.sql.*;
import java.util.*;
import model.Project;

public class ProjectDAO {
    public List<Project> getAllProjects() throws Exception {
        List<Project> list = new ArrayList<>();
        Connection conn = util.DBConnection.getConnection();
        String sql = "SELECT project_id, project_name FROM project_manage ORDER BY project_id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new Project(rs.getString("project_id"), rs.getString("project_name")));
        }
        rs.close(); stmt.close(); conn.close();
        return list;
    }
}