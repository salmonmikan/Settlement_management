package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Employee;
import util.DBConnection;

public class EmployeeDAO {

    // Hash password using SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Insert employee into staff table
    public boolean insertEmployee(Employee emp) {
        String sql = """
            INSERT INTO staff (staff_id, password, name, furigana, birth_date, address, hire_date, department_id, position_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getEmployeeId());
//            ps.setString(2, hashPassword(emp.getPassword()));
            ps.setString(2, emp.getPassword());
            ps.setString(3, emp.getFullName());
            ps.setString(4, emp.getFurigana());
            ps.setDate(5, emp.getBirthDate());
            ps.setString(6, emp.getAddress());
            ps.setDate(7, emp.getJoinDate());
            ps.setString(8, emp.getDepartmentId());
            ps.setString(9, emp.getPositionId());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy toàn bộ danh sách nhân viên
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT s.*,d.*,p.* "
        		+ "FROM staff as s "
        		+ "JOIN department_master as d ON s.department_id = d.department_id "
        		+ "JOIN position_master as p ON s.position_id = p.position_id;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getString("staff_id"));
                e.setPassword(rs.getString("password"));
                e.setFullName(rs.getString("name"));
                e.setFurigana(rs.getString("furigana"));
                e.setBirthDate(rs.getDate("birth_date"));
                e.setAddress(rs.getString("address"));
                e.setJoinDate(rs.getDate("hire_date"));
                e.setDepartmentName(rs.getString("department_name"));
                e.setPositionName(rs.getString("position_name"));
                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public Employee findByIdAndPassword(String staffId, String rawPassword) {
    	Employee staff = null;
        String sql = "SELECT * FROM staff WHERE staff_id = ? AND TRIM(password) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staffId);
//            stmt.setString(2, hashPassword(rawPassword));
            stmt.setString(2, rawPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                staff = new Employee();
                staff.setEmployeeId(rs.getString("staff_id"));
                staff.setFullName(rs.getString("name"));;
                staff.setPositionId(rs.getString("position_id"));; 
                //by Son
                staff.setDepartmentId(rs.getString("department_id"));;
                staff.setPassword(rs.getString("password"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return staff;
    }
    
    //IDから情報を取得
    public Employee findById(String staffId) {
        Employee emp = null;
        String sql = "SELECT * FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                emp = new Employee();
                emp.setEmployeeId(rs.getString("staff_id"));
                emp.setFullName(rs.getString("name"));
                emp.setFurigana(rs.getString("furigana"));
                emp.setBirthDate(rs.getDate("birth_date"));
                emp.setAddress(rs.getString("address"));
                emp.setJoinDate(rs.getDate("hire_date"));
                emp.setPassword(rs.getString("password"));
                emp.setDepartmentId(rs.getString("department_id"));
                emp.setPositionId(rs.getString("position_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return emp;
    }
 // UPDATE
    public boolean updateEmployee(Employee emp) {
        String sql = """
            UPDATE staff 
            SET password = ?, name = ?, furigana = ?, birth_date = ?, address = ?, hire_date = ?, department_id = ?, position_id = ?
            WHERE staff_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // ps.setString(1, hashPassword(emp.getPassword()));
            ps.setString(1, emp.getPassword());
            ps.setString(2, emp.getFullName());
            ps.setString(3, emp.getFurigana());
            ps.setDate(4, emp.getBirthDate());
            ps.setString(5, emp.getAddress());
            ps.setDate(6, emp.getJoinDate());
            ps.setString(7, emp.getDepartmentId());
            ps.setString(8, emp.getPositionId());
            ps.setString(9, emp.getEmployeeId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
   
    public boolean deleteEmployee(String staffId) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
