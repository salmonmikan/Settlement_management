package servlet;

import bean.BusinessTripBean.BusinessTripBean;
import dao.BusinessTripDAO;
import dao.ProjectDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Project;

import java.io.IOException;
import java.util.List;

@WebServlet("/editBusinessTrip")
public class EditBusinessTripServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int appId = Integer.parseInt(request.getParameter("id"));

            BusinessTripDAO dao = new BusinessTripDAO();
            BusinessTripBean trip = dao.loadBusinessTripByApplicationId(appId);

            request.setAttribute("step1Data", trip.getStep1Data());
            request.setAttribute("step2List", trip.getStep2List());
            request.setAttribute("step3List", trip.getStep3List());
            request.setAttribute("applicationId", appId);
            request.setAttribute("editMode", true); // フラグ追加
            
         // cần thêm đoạn này
            ProjectDAO projectDAO = new ProjectDAO();
            List<Project> projectList = projectDAO.getAllProjects();
            request.setAttribute("projectList", projectList);
            
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/edit/businessTrip1_edit.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("applicationMain.jsp");
        }
    }
}