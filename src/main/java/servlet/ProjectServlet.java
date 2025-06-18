package servlet;

import dao.ProjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Project;

import java.io.IOException;
import java.util.List;

@WebServlet("/project")
public class ProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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