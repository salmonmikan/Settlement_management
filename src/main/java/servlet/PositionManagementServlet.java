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


@WebServlet("/position_management_view")
public class PositionManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ProjectDAO dao = new ProjectDAO();
            List<ProjectList> projectList_management = dao.getAllProject_management();
            HttpSession session = req.getSession();
        	session.removeAttribute("errorMsg"); // 念のため。次の遷移先にエラー処理がある
            req.setAttribute("projectList_management", projectList_management);
            req.getRequestDispatcher("/position.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}