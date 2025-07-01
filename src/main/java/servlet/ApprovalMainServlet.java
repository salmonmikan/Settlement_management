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

@WebServlet("/approverApplications")
public class ApprovalMainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String approverId = (String) session.getAttribute("staffId");

        try {
            populateApplications(request, approverId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("applications", new java.util.ArrayList<Application>());
        }

        request.getRequestDispatcher("/WEB-INF/views/approvalMain.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String approverId = (String) session.getAttribute("staffId");
        String[] appIds = request.getParameterValues("appIds");
        String action = request.getParameter("action");

        try {
            ApplicationDAO dao = new ApplicationDAO();

            if (approverId == null || appIds == null || appIds.length == 0 || action == null) {
                request.setAttribute("message", "申請が選択されていません。");
                populateApplications(request, approverId);
                request.getRequestDispatcher("/WEB-INF/views/approvalMain.jsp").forward(request, response);
                return;
            }

            switch (action) {
                case "submit":
                    for (String idStr : appIds) {
                        int appId = Integer.parseInt(idStr);
                        dao.updateStatus(appId, approverId);
                    }
                    session.setAttribute("submitSuccess", true);
                    break;

                case "reject":
                    try {
                        for (String idStr : appIds) {
                            int appId = Integer.parseInt(idStr);
                            dao.rejectApplication(appId);
                        }
                        session.setAttribute("message", "選択された申請を差戻しました。");
                    } catch (Exception e) {
                        e.printStackTrace();
                        session.setAttribute("message", "差戻し中にエラーが発生しました。");
                    }
                    response.sendRedirect("approverApplications");
                    return;

                case "delete":
                    for (String idStr : appIds) {
                        int appId = Integer.parseInt(idStr);
                        dao.deleteApplication(appId);
                    }
                    session.setAttribute("message", "選択された申請を削除しました。");
                    break;

                default:
                    request.setAttribute("message", "不明な操作が指定されました。");
                    populateApplications(request, approverId);
                    request.getRequestDispatcher("/WEB-INF/views/approvalMain.jsp").forward(request, response);
                    return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "操作中にエラーが発生しました。");
            try {
                populateApplications(request, approverId);
            } catch (Exception ex) {
                ex.printStackTrace();
                request.setAttribute("applications", new java.util.ArrayList<Application>());
            }
            request.getRequestDispatcher("/WEB-INF/views/approvalMain.jsp").forward(request, response);
            return;
        }

        // Xử lý thành công → load lại danh sách
        response.sendRedirect("approverApplications");
    }

    private void populateApplications(HttpServletRequest request, String approverId) throws Exception {
        ApplicationDAO dao = new ApplicationDAO();
        String approver_depId = dao.findApproverDepartment(approverId);
        List<Application> applications = dao.getApplicationsByDepartment(approver_depId);
        request.setAttribute("applications", applications);
    }
}