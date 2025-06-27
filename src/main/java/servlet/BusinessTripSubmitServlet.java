package servlet;

import bean.BusinessTripBean.*;
import bean.BusinessTripBean.BusinessTripBean;
import dao.BusinessTripDAO;
import dao.ReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/submitBusinessTrip")
public class BusinessTripSubmitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        String staffId = (String) session.getAttribute("staffId");
        BusinessTripBean tripBean = (BusinessTripBean) session.getAttribute("businessTripBean");
        Map<String, List<String>> receiptMap = (Map<String, List<String>>) session.getAttribute("receiptMap");

        if (staffId == null || tripBean == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            // Tạo DAO
            BusinessTripDAO tripDAO = new BusinessTripDAO();
            ReceiptDAO receiptDAO = new ReceiptDAO();

            // Bước 1: insert application_header
            int applicationId = tripDAO.insertApplicationHeader(
                staffId,
                "出張費",
                tripBean.getTotalStep2Amount() + tripBean.getTotalStep3Amount()
            );

            // Bước 2: insert business_trip_application
            int tripId = tripDAO.insertBusinessTripApplication(
                applicationId,
                tripBean.getStep1Data()
            );

            // Bước 3: insert allowance_detail (Step2)
            tripDAO.insertAllowanceDetails(tripId, tripBean.getStep2List());

            // Bước 4: insert transportation_detail (Step3)
            tripDAO.insertTransportationDetails(tripId, tripBean.getStep3List());

            // Bước 5: insert receipt_mapping
            if (receiptMap != null) {
                receiptDAO.insertMapping(receiptMap, tripId);
            }

            // Sau khi lưu xong, xoá khỏi session
            session.removeAttribute("businessTripBean");
            session.removeAttribute("receiptMap");

            req.setAttribute("message", "出張費申請が正常に送信されました。");
            req.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "エラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}