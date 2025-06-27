package servlet;

import dao.PaymentDAO;
import bean.PaymentBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PaymentDAO dao = new PaymentDAO();
        ArrayList<PaymentBean> list = dao.findAll();

        req.setAttribute("paymentList", list);
        req.getRequestDispatcher("/payment.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String[] appIds = req.getParameterValues("appIds");

        if (appIds != null) {
            PaymentDAO dao = new PaymentDAO();
            for (String id : appIds) {
                dao.updateStatusToPaid(Integer.parseInt(id));
            }
        }

        resp.sendRedirect(req.getContextPath() + "/payment");
    }
}
