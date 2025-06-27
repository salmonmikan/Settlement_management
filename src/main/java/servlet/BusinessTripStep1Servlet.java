package servlet;

import bean.BusinessTripForm;
import dao.ProjectListDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/businessTrip")
public class BusinessTripStep1Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // === POST: xử lý dữ liệu step 1 ===
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        BusinessTripForm form = (BusinessTripForm) session.getAttribute("tripForm");
        if (form == null) {
            form = new BusinessTripForm();
        }

        // Lấy dữ liệu từ form
        String startDate = req.getParameter("startDateHidden");
        String endDate = req.getParameter("endDateHidden");
        String projectCode = req.getParameter("projectCode");
        String tripReport = req.getParameter("tripReport");
        String totalDaysStr = req.getParameter("totalDays");

        int totalDays = 0;
        try {
            totalDays = Integer.parseInt(totalDaysStr);
        } catch (NumberFormatException e) {
            totalDays = 1;
        }

        // Gán vào form
        form.setStartDate(startDate);
        form.setEndDate(endDate);
        form.setProjectCode(projectCode);
        form.setTripReport(tripReport);
        form.setTotalDays(totalDays);

        // Lưu lại vào session
        session.setAttribute("tripForm", form);

        // Chuyển tiếp sang step 2
        res.sendRedirect(req.getContextPath() + "/businessTripStep2");
    }

    // === GET: render lại form step 1 với dữ liệu đã có + project list ===
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        BusinessTripForm form = (BusinessTripForm) session.getAttribute("tripForm");

        if (form != null) {
            req.setAttribute("step1Data", form);
        }

        // Lấy danh sách project từ DB
        ProjectListDAO pjDAO = new ProjectListDAO();
        List<ProjectListDAO.Project> projectList = pjDAO.getAllProjects();
        req.setAttribute("projectList", projectList);

        // Forward đến JSP step1
        req.getRequestDispatcher("/WEB-INF/views/businessTrip/businessTrip1.jsp").forward(req, res);
    }
}