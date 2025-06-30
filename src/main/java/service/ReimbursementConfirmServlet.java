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
        
        if (session == null || session.getAttribute("reimbursement") == null) {
            response.sendRedirect(request.getContextPath() + "/reimbursementInit");
            return;
        }

        ReimbursementApplicationBean reimbursement = (ReimbursementApplicationBean) session.getAttribute("reimbursement");
        reimbursement.calculateTotalAmount();

        Boolean isEditMode = (Boolean) session.getAttribute("isEditMode");
        if (isEditMode == null) {
            isEditMode = false;
        }

        request.setAttribute("application_type", "立替金");
        request.setAttribute("reimbursementApp", reimbursement); 

        if (isEditMode) {
            request.setAttribute("isEditMode", true);
            request.setAttribute("showBackButton", true);
            request.setAttribute("backActionUrl", "/reimbursement");
            request.setAttribute("showSubmitButton", true);
            request.setAttribute("submitActionUrl", "/reimbursementUpdate");
        } else {
            request.setAttribute("isEditMode", false);
            request.setAttribute("showBackButton", true);
            request.setAttribute("backActionUrl", "/reimbursement");
            request.setAttribute("showSubmitButton", true);
            request.setAttribute("submitActionUrl", "/reimbursementSubmit");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp");
        rd.forward(request, response);
    }
}