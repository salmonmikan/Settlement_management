package servlet;

import dao.ApplicationDAO;
import model.Application;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/applicationMain")
public class ApplicationListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // ✅ Đưa position ra để dùng trong JSP
        String position = (String) session.getAttribute("position");
        request.setAttribute("position", position);

        // ✅ Hiển thị message thành công (tồn tại từ submit)
        Boolean submitSuccess = (Boolean) session.getAttribute("submitSuccess");
        if (submitSuccess != null && submitSuccess) {
            request.setAttribute("submitSuccess", true);
            session.removeAttribute("submitSuccess");
        }

        // ✅ Hiển thị message lỗi nếu có
        String message = (String) session.getAttribute("message");
        if (message != null) {
            request.setAttribute("message", message);
            session.removeAttribute("message");
        }

        // ✅ Lấy danh sách application
        String staffId = (String) session.getAttribute("staffId");

        List<Application> applications;
        try {
            applications = new ApplicationDAO().getApplicationsByStaffId(staffId);
        } catch (Exception e) {
            e.printStackTrace();
            applications = new ArrayList<>();
        }

        request.setAttribute("applications", applications);
        request.getRequestDispatcher("/WEB-INF/views/applicationMain.jsp").forward(request, response);
    }
}