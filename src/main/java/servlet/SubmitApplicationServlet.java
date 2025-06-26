// SubmitApplicationServlet.java
package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Application;
import dao.ApplicationDAO;

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

            // Check if all applications are still "未提出"
            for (String idStr : appIds) {
                int appId = Integer.parseInt(idStr);
                String status = dao.getApplicationStatus(appId);

                if (!"未提出".equals(status)) {
                    request.setAttribute("message", "未提出の申請のみ提出可能です。");
                    request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
                    return;
                }
            }

            // Get the position of current user
            String position = dao.getStaffPosition(staffId);

            // Get approver (部長 if not self)
            String managerId = null;
            if (!"部長".equals(position)) {
                managerId = dao.findManagerId(staffId);
                if (managerId == null) {
                    request.setAttribute("error", "部長が見つかりませんでした。");
                    request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
                    return;
                }
            }

            // Update applications
            for (String idStr : appIds) {
                int appId = Integer.parseInt(idStr);
                dao.submitApplicationIfNotYet(appId, staffId);
                // Optional: dao.setApprover(appId, managerId);
            }

            session.setAttribute("submitSuccess", true);
            response.sendRedirect("applicationMain");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "提出中にエラーが発生しました。");

            try {
                ApplicationDAO dao = new ApplicationDAO();
                List<Application> apps = dao.getApplicationsByStaffId(staffId);
                request.setAttribute("applications", apps);
            } catch (Exception ex) {
                ex.printStackTrace();
                request.setAttribute("applications", new ArrayList<Application>());
            }

            request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
        }
    }
}