//package servlet;
//
//import dao.ApplicationDAO;
//import dao.ProjectDAO;
//import bean.BusinessTripApplication;
//import model.Project;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//
//import java.io.IOException;
//import java.util.List;
//import bean.BusinessTripForm;
//@WebServlet("/initBusinessTrip")
//public class BusinessTripInitServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        HttpSession session = request.getSession();
//        String staffId = (String) session.getAttribute("staffId");
//
//        if (staffId == null) {
//            response.sendRedirect(request.getContextPath() + "/login.jsp");
//            return;
//        }
//
//        try {
//            // 1. Tạo application mới
//            ApplicationDAO dao = new ApplicationDAO();
//            int applicationId = dao.insertApplication("出張申請", staffId, 0);
//            session.setAttribute("applicationId", applicationId);
//            BusinessTripApplication tripApp = new BusinessTripApplication();
//            tripApp.setApplicationId(applicationId);
//            tripApp.setStaffId(staffId);
//
//            BusinessTripForm formData = new BusinessTripForm();
//            formData.setTripApplication(tripApp);
//
//            session.setAttribute("btFormData", formData);
//
//            // 2. Lấy tất cả dự án để hiển thị trong step1
//            ProjectDAO projectDAO = new ProjectDAO();
//            List<Project> projectList = projectDAO.getAllProjects();
//            request.setAttribute("projectList", projectList);
//
//            // 3. Debug log (nên in ra trước forward)
//            System.out.println(">> StaffId = " + staffId);
//            System.out.println(">> ProjectList size = " + projectList.size());
//
//            // 4. Forward sang step1
//            request.getRequestDispatcher("/WEB-INF/views/businessTrip/businessTrip1.jsp")
//                   .forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "出張申請の初期化に失敗しました。");
//            request.getRequestDispatcher("/error.jsp").forward(request, response);
//        }
//    }
//}