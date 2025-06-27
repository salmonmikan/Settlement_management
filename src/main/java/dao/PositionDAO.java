package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.PositionBean;
import util.DBConnection;

public class PositionDAO {

    // 全件取得（論理削除除く 0:通常, 9:削除不可）
    public ArrayList<PositionBean> findAll() {
        ArrayList<PositionBean> list = new ArrayList<>();
        String sql = "SELECT * FROM position_master WHERE delete_flag IN (0, 9) ORDER BY position_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PositionBean bean = new PositionBean();
                bean.setPosition_id(rs.getString("position_id"));
                bean.setPosition_name(rs.getString("position_name"));
                bean.setDelete_flag(rs.getInt("delete_flag")); // 追加
                list.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ID検索（論理削除されてないもの限定）
    public PositionBean findById(String id) {
        PositionBean bean = null;
        String sql = "SELECT * FROM position_master WHERE position_id = ? AND delete_flag IN (0, 9)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bean = new PositionBean();
                bean.setPosition_id(rs.getString("position_id"));
                bean.setPosition_name(rs.getString("position_name"));
                bean.setDelete_flag(rs.getInt("delete_flag")); // 追加
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    // 登録
    public boolean insert(PositionBean bean) {
        String sql = "INSERT INTO position_master (position_id, position_name, delete_flag) VALUES (?, ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bean.getPosition_id());
            ps.setString(2, bean.getPosition_name());
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新（delete_flag = 0 のみ対象）
    public boolean update(PositionBean bean) {
        String sql = "UPDATE position_master SET position_name = ? WHERE position_id = ? AND delete_flag = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bean.getPosition_name());
            ps.setString(2, bean.getPosition_id());
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 論理削除（delete_flag = 0 のみ対象、9は削除不可）
    public boolean delete(String id) {
        String sql = "UPDATE position_master SET delete_flag = 1 WHERE position_id = ? AND delete_flag = 0";

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
