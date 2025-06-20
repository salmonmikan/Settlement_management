package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import bean.BusinessTripBean.ReimbursementBean;
import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import util.DBConnection;

public class ReimbursementDAO {

    /**
     * Lưu toàn bộ dữ liệu từ ReimbursementBean (step1 + step2 list) vào DB.
     * Mỗi Step2Data là một dòng trong bảng reimbursement_request, lặp lại thông tin step1.
     * @param bean ReimbursementBean chứa step1 và danh sách step2
     * @param employeeId ID người đăng nhập
     * @return baseId gốc (ví dụ: "R-") để hiển thị lại nếu cần
     */
    public String insertReimbursement(ReimbursementBean bean, String employeeId) {
    	String sql = "INSERT INTO reimbursement_request " +
                "(reimbursement_request_id, employee_id, project_code, date, accounting_item, amount, memo, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Step1Data s1 = bean.getStep1();
        List<Step2Detail> s2List = bean.getStep2List();

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int counter = 1;
            for (Step2Detail s2 : s2List) {
                // ID dạng R-000001, R-000002,...
                String requestId = String.format("R-%06d", System.currentTimeMillis() % 1000000 + counter);

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, requestId);
                    stmt.setString(2, employeeId);
                    stmt.setString(3, s1.getProjectCode());
                    stmt.setDate(4, Date.valueOf(s1.getStartDate()));
                    stmt.setString(5, s2.getAccountingItem());
                    stmt.setInt(6, s2.getAmount());
                    stmt.setString(7, s2.getMemo());
                    stmt.setString(8, "pending");

                    stmt.executeUpdate();
                }

                counter++;
            }

            conn.commit();
            return "R-"; // hoặc trả về requestId đầu tiên nếu bạn muốn

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
