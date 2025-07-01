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

        // Thiết lập các thuộc tính cho các nút bấm trên trang confirm
        request.setAttribute("showBackButton", true);
        request.setAttribute("showSubmitButton", true);
        request.setAttribute("showEditButton", false); // Tùy chỉnh nếu cần
        request.setAttribute("backActionUrl", "/transportationRequest"); // URL để quay lại form
        request.setAttribute("submitActionUrl", "/transportationSubmit"); // URL để submit cuối cùng

        // TODO: Thay thế bằng đường dẫn đến file confirm.jsp của bạn
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp"); 
        rd.forward(request, response);
    }
}