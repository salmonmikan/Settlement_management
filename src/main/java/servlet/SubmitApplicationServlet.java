package servlet;

import dao.ApplicationDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Application;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/submitApplication")
public class SubmitApplicationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String staffId = (String) session.getAttribute("staffId");
        String[] appIds = request.getParameterValues("appIds");

        if (appIds == null || appIds.length == 0 || staffId == null) {
            response.sendRedirect("applicationMain");
            return;
        }

        try {
            ApplicationDAO dao = new ApplicationDAO();

            // Kiểm tra xem tất cả đơn có phải 未提出 không
            for (String idStr : appIds) {
                int appId = Integer.parseInt(idStr);
                String status = dao.getApplicationStatus(appId);

                if (!"未提出".equals(status)) {
                    request.setAttribute("message", "未提出の申請のみ提出可能です。");
                    List<Application> apps = dao.getApplicationsByStaffId(staffId);
                    request.setAttribute("applications", apps);
                    request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
                    return;
                }
            }

            // Lấy chức vụ của người dùng hiện tại
            String position = dao.getStaffPosition(staffId);

            // Lấy approver là 部長 nếu người gửi không phải là 部長
            String managerId = null;
            if (!"部長".equals(position)) {
                managerId = dao.findManagerId(staffId);
                if (managerId == null) {
                    request.setAttribute("message", "部長が見つかりませんでした。");
                    List<Application> apps = dao.getApplicationsByStaffId(staffId);
                    request.setAttribute("applications", apps);
                    request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
                    return;
                }
            }

            // Cập nhật status + approver cho từng đơn
            for (String idStr : appIds) {
                int appId = Integer.parseInt(idStr);
                dao.submitApplicationIfNotYet(appId, staffId, managerId); // sửa hàm này để set approver_id trong cùng câu lệnh
            }

            session.setAttribute("submitSuccess", true);
            response.sendRedirect("applicationMain");
            return;

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "提出中にエラーが発生しました。");
            response.sendRedirect("applicationMain");
        }
    }
}