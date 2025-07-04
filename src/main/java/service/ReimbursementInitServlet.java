
package service;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.ReimbursementApplicationBean;

@WebServlet("/reimbursementInit")
public class ReimbursementInitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        ReimbursementApplicationBean reimbursement = new ReimbursementApplicationBean();
        session.setAttribute("reimbursement", reimbursement);
        
        session.setAttribute("isEditMode", false);

        // Chuyển hướng (redirect) đến servlet chính để hiển thị form
        // Redirect để đảm bảo URL trên trình duyệt là /reimbursement, không phải /reimbursementInit
        response.sendRedirect(request.getContextPath() + "/reimbursement");
    }
}