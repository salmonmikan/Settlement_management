// File: src/main/java/service/BusinessTripInitServlet.java
// (Phiên bản ĐÚNG)
package service;

import java.io.IOException;
import bean.BusinessTripBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/businessTripInit")
public class BusinessTripInitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. Tạo một BusinessTripBean mới, rỗng
        BusinessTripBean trip = new BusinessTripBean();
        
        // 2. Lưu bean vào session
        session.setAttribute("trip", trip);

        // 3. DÙNG REDIRECT để yêu cầu trình duyệt gọi đến /businessTripStep1
        // Thao tác này sẽ kích hoạt method doGet của BusinessTripStep1Servlet
        response.sendRedirect(request.getContextPath() + "/businessTripStep1");
    }
}