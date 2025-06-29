
package service;

import java.io.IOException;
import bean.ReimbursementApplicationBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/reimbursementInit")
public class ReimbursementInitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Luôn tạo một đối tượng Bean mới, trắng tinh
        ReimbursementApplicationBean reimbursement = new ReimbursementApplicationBean();
        
        // Ghi đè Bean cũ (nếu có) trong session bằng Bean mới này
        session.setAttribute("reimbursement", reimbursement);

        // Chuyển hướng (redirect) đến servlet chính để hiển thị form
        // Redirect để đảm bảo URL trên trình duyệt là /reimbursement, không phải /reimbursementInit
        response.sendRedirect(request.getContextPath() + "/reimbursement");
    }
}