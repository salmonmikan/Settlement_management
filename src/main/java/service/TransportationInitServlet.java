package service;

import java.io.IOException;
import bean.TransportationApplicationBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/transportationInit")
public class TransportationInitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Luôn tạo một đối tượng TransportationApplicationBean mới, trắng tinh
        TransportationApplicationBean transportationApp = new TransportationApplicationBean();
        
        // Ghi đè Bean cũ (nếu có) trong session bằng Bean mới này
        // Tên attribute là "transportationApp" để khớp với JSP
        session.setAttribute("transportationApp", transportationApp);
        session.setAttribute("isEditMode", false);
        // Chuyển hướng (redirect) đến servlet chính để hiển thị form
        response.sendRedirect(request.getContextPath() + "/transportationRequest");
    }
}