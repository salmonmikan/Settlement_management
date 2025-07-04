package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ApplicationDAO;
import model.Application;

@WebServlet("/applicationMain")
public class ApplicationListServlet extends HttpServlet {
    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("staffId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // --- Set thuộc tính để dùng trong JSP ---
        request.setAttribute("position", session.getAttribute("position"));

        // --- Xử lý thông báo thành công ---
        Boolean submitSuccess = (Boolean) session.getAttribute("submitSuccess");
        if (Boolean.TRUE.equals(submitSuccess)) {
            request.setAttribute("submitSuccess", true);
            session.removeAttribute("submitSuccess");
        }

        // --- Xử lý thông báo lỗi (nếu có) ---
        String message = (String) session.getAttribute("message");
        if (message != null) {
            request.setAttribute("message", message);
            session.removeAttribute("message");
        }

        // --- Lấy danh sách đơn đăng ký ---
        String staffId = (String) session.getAttribute("staffId");

        try {
            List<Application> applications = applicationDAO.getApplicationsByStaffId(staffId);
            request.setAttribute("applications", applications);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("applications", List.of()); // Trả về list rỗng nếu lỗi
            request.setAttribute("message", "申請一覧の取得中にエラーが発生しました。");
        }

        // --- Chuyển đến trang hiển thị ---
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/applicationMain.jsp")
               .forward(request, response);
    }
}