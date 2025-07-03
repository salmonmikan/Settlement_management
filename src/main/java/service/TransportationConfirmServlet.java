package service;

import java.io.IOException;
import bean.TransportationApplicationBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/transportationConfirm")
public class TransportationConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("transportationApp") == null) {
            response.sendRedirect(request.getContextPath() + "/transportationInit");
            return;
        }

        TransportationApplicationBean transportationApp = (TransportationApplicationBean) session.getAttribute("transportationApp");
        transportationApp.calculateTotalAmount(); // Tính tổng tiền

        request.setAttribute("application_type", "交通費");
        
        // Đặt bean vào request để JSP confirm có thể đọc được
        request.setAttribute("transportationApp", transportationApp); 
        
     // Lấy cờ isEditMode từ session
     Boolean isEditMode = (Boolean) session.getAttribute("isEditMode");
     if (isEditMode == null) {
         isEditMode = false; // Mặc định là luồng tạo mới nếu không có cờ
     }

     // Thiết lập các thuộc tính cho các nút bấm dựa trên isEditMode
     if (isEditMode) {
         // Đây là luồng UPDATE
         request.setAttribute("isEditMode", true); // Gửi cờ ra JSP để đổi chữ trên nút (nếu cần)
         request.setAttribute("showBackButton", true);
         request.setAttribute("backActionUrl", "/transportationRequest");
         request.setAttribute("showSubmitButton", true);
         request.setAttribute("submitActionUrl", "/transportationUpdate"); // Trỏ đến servlet Update

     } else {
         // Đây là luồng TẠO MỚI (Submit)
         request.setAttribute("isEditMode", false);
         request.setAttribute("showBackButton", true);
         request.setAttribute("backActionUrl", "/transportationRequest");
         request.setAttribute("showSubmitButton", true);
         request.setAttribute("submitActionUrl", "/transportationSubmit"); // Trỏ đến servlet Submit
     }

        // TODO: Thay thế bằng đường dẫn đến file confirm.jsp của bạn
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp"); 
        rd.forward(request, response);
    }
}