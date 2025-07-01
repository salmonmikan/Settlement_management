package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean;
import bean.Project;
import bean.Step1Data;
import dao.ProjectListDAO;

@WebServlet("/businessTripStep1")
public class BusinessTripStep1Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            ProjectListDAO projectDAO = new ProjectListDAO();
            List<ProjectListDAO.Project> daoProjectList = projectDAO.getAllProjects();

           
            List<Project> projectListForJsp = new ArrayList<>();
            
            for (ProjectListDAO.Project daoProject : daoProjectList) {
               
                Project jspProject = new Project(daoProject.getId(), daoProject.getName());
                
                
                projectListForJsp.add(jspProject);
            }

            
            request.setAttribute("projectList", projectListForJsp);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("projectList", new ArrayList<Project>());
        }
        
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/businessTrip1.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        Step1Data step1Data = trip.getStep1Data();

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String projectCode = request.getParameter("projectCode");
        String tripReport = request.getParameter("tripReport");

        step1Data.setStartDate(startDateStr);
        step1Data.setEndDate(endDateStr);
        step1Data.setProjectCode(projectCode);
        step1Data.setTripReport(tripReport);

        try {

        	LocalDate startDate = LocalDate.parse(startDateStr.replace('/', '-'));
            LocalDate endDate = LocalDate.parse(endDateStr.replace('/', '-'));

            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

            if (daysBetween > 0) {
                step1Data.setTotalDays((int) daysBetween); 
            } else {
                step1Data.setTotalDays(0); 
            }
        } catch (Exception e) {
            step1Data.setTotalDays(0); 
            System.err.println("Step 1がミスががる: " + e.getMessage());
        }
        session.setAttribute("trip", trip);
        response.sendRedirect(request.getContextPath() + "/businessTripStep2");
    }
}