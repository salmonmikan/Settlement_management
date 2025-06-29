package service;

import java.io.IOException;
import bean.ReimbursementApplicationBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/reimbursementConfirm")
public class ReimbursementConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // ★★★ SỬA LỖI 1: Nhất quán sử dụng tên session attribute là "reimbursement"
        if (session == null || session.getAttribute("reimbursement") == null) {
            response.sendRedirect(request.getContextPath() + "/reimbursementInit");
            return;
        }

        ReimbursementApplicationBean reimbursement = (ReimbursementApplicationBean) session.getAttribute("reimbursement");
        reimbursement.calculateTotalAmount(); // Tính tổng tiền

        request.setAttribute("application_type", "立替金");
        
        // ★★★ SỬA LỖI 2: Đặt bean vào request với tên là "reimbursementApp" để JSP đọc được
        request.setAttribute("reimbursementApp", reimbursement); 

        // Thiết lập các thuộc tính cho nút bấm
        request.setAttribute("showBackButton", true);
        request.setAttribute("showSubmitButton", true);
        request.setAttribute("showEditButton", false);
        request.setAttribute("backActionUrl", "/reimbursement"); 
        request.setAttribute("submitActionUrl", "/reimbursementSubmit"); 

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp");
        rd.forward(request, response);
    }
}