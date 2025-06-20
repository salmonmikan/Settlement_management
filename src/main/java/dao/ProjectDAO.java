package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Project;
import model.ProjectList;

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
    
    // プロジェクト管理画面用
    public List<ProjectList> getAllProject_management() throws Exception {
        List<ProjectList> list = new ArrayList<>();
        Connection conn = util.DBConnection.getConnection();
        String sql = """
            SELECT
                pi.project_code,
                pi.project_name,
                pi.project_owner,
                GROUP_CONCAT(p.full_name ORDER BY p.full_name SEPARATOR ', ') AS member_names,
                pi.start_date,
                pi.end_date,
                pb.project_budget,
                pb.project_actual
            FROM
                project_info pi
            JOIN
                project_management pm ON pi.project_code = pm.project_code
            JOIN
        		personal_info p ON pm.employee_id = p.employee_id
            LEFT JOIN
                project_budget pb ON pi.project_code = pb.project_code
            GROUP BY
                pi.project_code, pi.project_name, pi.project_owner, pi.start_date, pi.end_date, pb.project_budget, pb.project_actual
            ORDER BY
                pi.project_code
        """;
        		
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
        	ProjectList psl = new ProjectList();

            psl.setProject_code(rs.getString("project_code"));
            psl.setProject_name(rs.getString("project_name"));
            psl.setProject_owner(rs.getString("project_owner"));
            psl.setProject_members(rs.getString("member_names"));
            psl.setStart_date(rs.getString("start_date"));
            psl.setEnd_date(rs.getString("end_date"));
            psl.setProject_budget(rs.getObject("project_budget") != null ? rs.getInt("project_budget") : null);
            psl.setProject_actual(rs.getObject("project_actual") != null ? rs.getInt("project_actual") : null);
            
            list.add(psl);
        }
        rs.close(); stmt.close(); conn.close();
        return list;
    }
}