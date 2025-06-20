package servlet;

import dao.ApplicationDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;

@WebServlet("/submitApplication")
public class SubmitApplicationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String staffId = (String) session.getAttribute("staffId");
        String[] appIds = request.getParameterValues("appIds");

        if (appIds != null && staffId != null) {
            try {
                ApplicationDAO dao = new ApplicationDAO();

                // Tìm 部長 trong cùng phòng ban
                String managerId = dao.findManagerId(staffId);
                if (managerId == null) {
                    request.setAttribute("error", "部長が見つかりませんでした。");
                    request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
                    return;
                }

                for (String idStr : appIds) {
                    int appId = Integer.parseInt(idStr);
                    dao.submitApplicationIfNotYet(appId, staffId);
                    // Nếu có approver_id: dao.setApprover(appId, managerId);
                }

                // Đánh dấu flag để hiển thị message sau khi redirect
                session.setAttribute("submitSuccess", true);

                // Tránh lỗi IllegalStateException
                response.sendRedirect("applicationMain");
                return;

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "提出中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "申請が選択されていません。");
            request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
        }
    }
}