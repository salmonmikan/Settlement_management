package service;

import java.io.IOException;
import bean.TransportationApplicationBean;
import bean.TransportationDetailBean;
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

        transportationApp.getDetails().forEach(detail -> {
            int multiplier = "往復".equals(detail.getTransTripType()) ? 2 : 1;
            if ("自己".equals(detail.getBurden())) {
                detail.setExpenseTotal(detail.getFareAmount() * multiplier);
            } else {
                detail.setExpenseTotal(0);
            }
        });
        for (TransportationDetailBean detail : transportationApp.getDetails()) {
            System.out.println("確認: ID=" + detail.getTransportationId()
                + " / fare=" + detail.getFareAmount()
                + " / 区分=" + detail.getTransTripType()
                + " / 負担者=" + detail.getBurden()
                + " / 合計=" + detail.getExpenseTotal());
        }

        transportationApp.calculateTotalAmount();
        System.out.println(">>> TransportationApp hash: " + transportationApp.hashCode());
        System.out.println(">>> Total amount: " + transportationApp.getTotalAmount());

        request.setAttribute("application_type", "交通費");
        request.setAttribute("transportationApp", transportationApp); 

        request.setAttribute("showBackButton", true);
        request.setAttribute("showSubmitButton", true);
        request.setAttribute("showEditButton", false);
        request.setAttribute("backActionUrl", "/transportationRequest");
        request.setAttribute("submitActionUrl", "/transportationSubmit");

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp"); 
        rd.forward(request, response);
        System.out.println("=== TRANSPORTATION CONFIRM SERVLET ===");
        System.out.println("totalAmount = " + transportationApp.getTotalAmount());
    }
}