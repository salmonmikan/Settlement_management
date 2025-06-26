package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import bean.Project;
import dao.ProjectDAO;

@WebServlet("/projectList")
public class ProjectListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ProjectDAO dao = new ProjectDAO();
            List<Project> projectList = dao.getAllProjects();
            req.setAttribute("projectList", projectList);
            req.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}