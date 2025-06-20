package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.DepartmentBean;
import util.DBConnection;

// 部署の一覧表示、新規追加、更新、削除のDAO
public class DepartmentDAO {
    public ArrayList<DepartmentBean> all_get() {
        ArrayList<DepartmentBean> list = new ArrayList<DepartmentBean>();
        String sql = "SELECT * FROM department_master";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

//            stmt.setString(1, id);
//            stmt.setString(2, name);

            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                DepartmentBean bean = new DepartmentBean();
                bean.setDepartment_id(rs.getString("department_id"));
                bean.setDepartment_name(rs.getString("department_name"));
                list.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    
    }
    public void insert(String id, String name) {
        String sql = "INSERT INTO department_master (department_id, department_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setString(2, name);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update(String id, String name) {
        String sql = "UPDATE department_master SET department_name = ? WHERE department_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, id);
            
            
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void delete(String department_id) {
        String sql = "DELETE FROM department_master WHERE department_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department_id);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}