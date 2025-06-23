package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
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

    // Insert employee into 3 tables
    public boolean insertEmployee(Employee emp) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // personal_info
            String sql1 = "INSERT INTO personal_info (employee_id, full_name, furigana, birth_date, address, join_date) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, emp.getEmployeeId());
            ps1.setString(2, emp.getFullName());
            ps1.setString(3, emp.getFurigana());
            ps1.setDate(4, emp.getBirthDate());
            ps1.setString(5, emp.getAddress());
            ps1.setDate(6, emp.getJoinDate());
            ps1.executeUpdate();

            // account_info
            String sql2 = "INSERT INTO account_info (employee_id, login_id, password) VALUES (?, ?, ?)";
            ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, emp.getEmployeeId());
            ps2.setString(2, emp.getLoginId());
            ps2.setString(3, hashPassword(emp.getPassword()));
            ps2.executeUpdate();

            // specification_info
            String sql3 = "INSERT INTO specification_info (employee_id, department_id, position_id) VALUES (?, ?, ?)";
            ps3 = conn.prepareStatement(sql3);
            ps3.setString(1, emp.getEmployeeId());
            ps3.setString(2, emp.getDepartmentId());
            ps3.setString(3, emp.getPositionId());
            ps3.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try { if (ps3 != null) ps3.close(); } catch (Exception e) {}
            try { if (ps2 != null) ps2.close(); } catch (Exception e) {}
            try { if (ps1 != null) ps1.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = """
            SELECT 
                p.employee_id,
                p.full_name,
                p.furigana,
                p.birth_date,
                p.address,
                p.join_date,
                a.login_id,
                a.password,
                d.department_id,
                d.department_name,
                pos.position_id,
                pos.position_name
            FROM personal_info p
            JOIN account_info a ON p.employee_id = a.employee_id
            JOIN specification_info s ON p.employee_id = s.employee_id
            JOIN department d ON s.department_id = d.department_id
            JOIN position pos ON s.position_id = pos.position_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getString("employee_id"));
                e.setFullName(rs.getString("full_name"));
                e.setFurigana(rs.getString("furigana"));
                e.setBirthDate(rs.getDate("birth_date"));
                e.setAddress(rs.getString("address"));
                e.setJoinDate(rs.getDate("join_date"));
                e.setLoginId(rs.getString("login_id"));
                e.setPassword(rs.getString("password"));
                e.setDepartmentId(rs.getString("department_id") + " : " + rs.getString("department_name"));
                e.setPositionId(rs.getString("position_id") + " : " + rs.getString("position_name"));

                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    //LOGIN CHECK
    public Employee login(String loginId, String rawPassword) {
        String sql = """
            SELECT 
                p.employee_id,
                p.full_name,
                a.login_id,
                a.password,
                d.department_name,
                pos.position_name
            FROM personal_info p
            JOIN account_info a ON p.employee_id = a.employee_id
            JOIN specification_info s ON p.employee_id = s.employee_id
            JOIN department_master d ON s.department_id = d.department_id
            JOIN position_master pos ON s.position_id = pos.position_id
            WHERE a.login_id = ? AND a.password = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String hashed = hashPassword(rawPassword);
            ps.setString(1, loginId);
            ps.setString(2, hashed);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee e = new Employee();
                    e.setEmployeeId(rs.getString("employee_id"));
                    e.setFullName(rs.getString("full_name"));
                    e.setLoginId(rs.getString("login_id"));
                    e.setPassword(rs.getString("password"));
                    e.setDepartmentId(rs.getString("department_name"));
                    e.setPositionId(rs.getString("position_name"));
                    return e;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
