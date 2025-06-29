package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.ProjectList;
import dao.ProjectDAO;

@WebServlet("/project_management_view")
public class ProjectManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ProjectDAO dao = new ProjectDAO();
            List<ProjectList> projectList_management = dao.getAllProject_management();
            req.setAttribute("projectList_management", projectList_management);

            // ここから：セッションからメッセージを取得
            HttpSession session = req.getSession();
            String successMsg = (String) session.getAttribute("successMsg");
            String errorMsg = (String) session.getAttribute("errorMsg");

            if (successMsg != null) {
                req.setAttribute("successMsg", successMsg);
                session.removeAttribute("successMsg");
            }
            if (errorMsg != null) {
                req.setAttribute("errorMsg", errorMsg);
                session.removeAttribute("errorMsg");
            }
            // ここまで

            req.getRequestDispatcher("/WEB-INF/views/projectList.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
