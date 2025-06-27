package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.DepartmentBean;
import util.DBConnection;

public class DepartmentDAO {

    // 全件取得（論理削除除く 0:通常, 9:削除不可）
    public ArrayList<DepartmentBean> findAll() {
        ArrayList<DepartmentBean> list = new ArrayList<>();
        String sql = "SELECT * FROM department_master WHERE delete_flag IN (0, 9) ORDER BY department_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    // ID検索（論理削除されてないもの限定）
    public DepartmentBean findById(String id) {
        DepartmentBean bean = null;
        String sql = "SELECT * FROM department_master WHERE department_id = ? AND delete_flag IN (0, 9)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bean = new DepartmentBean();
                bean.setDepartment_id(rs.getString("department_id"));
                bean.setDepartment_name(rs.getString("department_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    // 登録
    public boolean insert(DepartmentBean bean) {
        String sql = "INSERT INTO department_master (department_id, department_name, delete_flag) VALUES (?, ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bean.getDepartment_id());
            ps.setString(2, bean.getDepartment_name());
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新（delete_flag = 0 のみ対象）
    public boolean update(DepartmentBean bean) {
        String sql = "UPDATE department_master SET department_name = ? WHERE department_id = ? AND delete_flag = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bean.getDepartment_name());
            ps.setString(2, bean.getDepartment_id());
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 論理削除（delete_flag = 0 のみ対象、9は削除不可）
    public boolean delete(String id) {
        String sql = "UPDATE department_master SET delete_flag = 1 WHERE department_id = ? AND delete_flag = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
