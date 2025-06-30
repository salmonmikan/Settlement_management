package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ApplicationDAO;
import model.Application;

@WebServlet("/submitApplication")
public class SubmitApplicationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String staffId = (String) session.getAttribute("staffId");
        String[] appIds = request.getParameterValues("appIds");
        String action = request.getParameter("action");

        if (staffId == null || appIds == null || appIds.length == 0 || action == null) {
            response.sendRedirect("applicationMain");
            return;
        }

        try {
            ApplicationDAO dao = new ApplicationDAO();

            switch (action) {
                case "post":
                    for (String idStr : appIds) {
                        int appId = Integer.parseInt(idStr);
                        String status = dao.getApplicationStatus(appId);
                        if (!"未提出".equals(status)) {
                            request.setAttribute("message", "未提出の申請のみ提出可能です。");
                            List<Application> apps = dao.getApplicationsByStaffId(staffId);
                            request.setAttribute("applications", apps);
                            request.getRequestDispatcher("/WEB-INF/views/serviceJSP/applicationMain.jsp").forward(request, response);
                            return;
                        }
                    }

                    for (String idStr : appIds) {
                        int appId = Integer.parseInt(idStr);
                        dao.submitApplicationIfNotYet(appId, staffId, "");
                    }

                    session.setAttribute("submitSuccess", true);
                    break;

                case "edit":
                    if (appIds.length == 1) {
                        int appId = Integer.parseInt(appIds[0]);
                        response.sendRedirect("applicationDetail?id=" + appId);
                        return;
                    } else {
                        request.setAttribute("message", "編集は1件のみ選択してください。");
                    }
                    break;

                case "delete":
                    for (String idStr : appIds) {
                        int appId = Integer.parseInt(idStr);
                        dao.deleteApplication(appId); // 論理削除
                    }
                    request.setAttribute("message", "選択された申請を削除しました。");
                    break;

                default:
                    request.setAttribute("message", "不明な操作です。");
            }

            // 一覧を再取得して表示
            List<Application> apps = dao.getApplicationsByStaffId(staffId);
            request.setAttribute("applications", apps);
            request.getRequestDispatcher("/WEB-INF/views/serviceJSP/applicationMain.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "操作中にエラーが発生しました。");
            response.sendRedirect("applicationMain");
        }
    }
}
