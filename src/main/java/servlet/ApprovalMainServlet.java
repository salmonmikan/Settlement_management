package servlet;

import dao.ApplicationDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import model.Application;

import java.io.IOException;
import java.util.List;

@WebServlet("/approverApplications")
public class ApprovalMainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        String approverId = (String) session.getAttribute("staffId");

        try {
            ApplicationDAO dao = new ApplicationDAO();
            List<Application> applications = dao.getApplicationsByApprover(approverId);
            request.setAttribute("applications", applications);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("applications", new java.util.ArrayList<Application>());
        }

        request.getRequestDispatcher("/WEB-INF/views/approvalMain.jsp").forward(request, response);
    }
}