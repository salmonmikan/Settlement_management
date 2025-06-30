// File: service/BusinessTripConfirmServlet.java (Phiên bản cập nhật)
package service;

import java.io.IOException;
import bean.BusinessTripBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/businessTripConfirm")
public class BusinessTripConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        trip.calculateTotalAmount();
        
        // Lấy cờ isEditMode từ session
        Boolean isEditMode = (Boolean) session.getAttribute("isEditMode");
        if (isEditMode == null) {
            isEditMode = false;
        }

        request.setAttribute("application_type", "出張費申請");
        request.setAttribute("trip", trip);
        
        // === LOGIC MỚI: Tùy chỉnh nút bấm dựa trên isEditMode ===
        if (isEditMode) {
            // Đây là luồng UPDATE
            request.setAttribute("isEditMode", true); // Gửi cờ này ra JSP để đổi chữ trên nút
            request.setAttribute("showBackButton", true);
            request.setAttribute("backActionUrl", "/businessTripStep3");
            request.setAttribute("showSubmitButton", true);
            request.setAttribute("submitActionUrl", "/businessTripUpdate"); // Action trỏ đến servlet Update mới
        } else {
            // Đây là luồng TẠO MỚI (như cũ)
            request.setAttribute("isEditMode", false);
            request.setAttribute("showBackButton", true);
            request.setAttribute("backActionUrl", "/businessTripStep3");
            request.setAttribute("showSubmitButton", true);
            request.setAttribute("submitActionUrl", "/businessTripSubmit");
        }
        
        request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp").forward(request, response);
    }
}