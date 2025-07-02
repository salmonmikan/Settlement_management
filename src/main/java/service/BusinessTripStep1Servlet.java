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
import dao.ProjectDAO;
import dao.ProjectListDAO;

/**
 * 出張申請のステップ1を処理するサーブレット。
 * (Servlet xử lý Step 1 của đơn đăng ký công tác)
 */
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

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String projectCode = request.getParameter("projectCode");
        String tripReport = request.getParameter("tripReport");

        // ================== ✅ LOGIC SỬA LỖI BẮT ĐẦU TỪ ĐÂY ==================

        if (startDateStr == null || startDateStr.length() < 10 || 
            endDateStr == null || endDateStr.length() < 10) {
            
            request.setAttribute("errorMessage", "日付を正しく入力してください。");
            
           
            doGet(request, response); 
            return; // Dừng xử lý
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        Step1Data step1Data = trip.getStep1Data();

        step1Data.setStartDate(startDateStr);
        step1Data.setEndDate(endDateStr);
        step1Data.setProjectCode(projectCode);
        step1Data.setTripReport(tripReport);

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            
            if (daysBetween > 0) {
                step1Data.setTotalDays((int) daysBetween);
            } else {
                request.setAttribute("errorMessage", "終了日は開始日以降に設定してください。");
                doGet(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "日付の形式が無効です。");
            doGet(request, response);
            return;
        }

        session.setAttribute("trip", trip);
        response.sendRedirect(request.getContextPath() + "/businessTripStep2");
    }
}