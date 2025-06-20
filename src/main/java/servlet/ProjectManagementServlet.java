package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ProjectDAO;
import model.ProjectList;

// プロジェクト管理表示用
@WebServlet("/project_management")
public class ProjectManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ProjectDAO dao = new ProjectDAO();
            List<ProjectList> projectList_management = dao.getAllProject_management();
            req.setAttribute("projectList_management", projectList_management);
            req.getRequestDispatcher("/WEB-INF/views/projectList.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}