package dao;

import java.sql.*;
import java.util.*;
import model.Project;
import util.DBConnection;

public class ProjectListDAO {
    public List<Project> getAllProjects() {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT project_code, project_name FROM project_manage ORDER BY project_code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Project p = new Project();
                p.setId(rs.getString("project_code"));
                p.setName(rs.getString("project_name"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static class Project {
        private String id;
        private String name;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}