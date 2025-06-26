package servlet;

import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;
import dao.ApplicationDAO;
import dao.BusinessTripDAO;
import dao.ReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/submitBusinessTrip")
@MultipartConfig
public class SubmitBusinessTripServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            BusinessTripBean bt = (BusinessTripBean) session.getAttribute("businessTripBean");
            String staffId = (String) session.getAttribute("staffId");

            if (bt == null || staffId == null) {
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

            int totalAmount = bt.getTotalStep2Amount() + bt.getTotalStep3Amount();

            boolean editMode = Boolean.parseBoolean(request.getParameter("editMode"));
            int applicationId;
            ApplicationDAO appDAO = new ApplicationDAO();
            BusinessTripDAO tripDAO = new BusinessTripDAO();
            ReceiptDAO receiptDAO = new ReceiptDAO();

            if (editMode) {
                applicationId = Integer.parseInt(request.getParameter("applicationId"));
                appDAO.updateApplicationAmount(applicationId, totalAmount);
            } else {
                applicationId = appDAO.insertApplication("出張費", staffId, totalAmount);
            }

            // Step 1
            int tripAppId = tripDAO.insertBusinessTripApplication(applicationId, bt.getStep1Data());

            // Step 2
            tripDAO.deleteAllowanceDetails(tripAppId);
            List<String> step2Files = getFilesFromSession(session, "step2");
            int i = 0;
            for (Step2Detail s2 : bt.getStep2List()) {
                int detailId = tripDAO.insertAllowanceDetail(tripAppId, s2);
                if (i < step2Files.size()) {
                    String filePath = step2Files.get(i++);
                    String originalName = extractOriginalName(filePath);
                    receiptDAO.insertReceipt("allowance_detail", detailId, originalName, filePath);
                }
            }

            // Step 3
            tripDAO.deleteTransportDetails(tripAppId);
            List<String> step3Files = getFilesFromSession(session, "step3");
            int j = 0;
            for (Step3Detail s3 : bt.getStep3List()) {
                int detailId = tripDAO.insertTransportDetail(tripAppId, s3);
                if (j < step3Files.size()) {
                    String filePath = step3Files.get(j++);
                    String originalName = extractOriginalName(filePath);
                    receiptDAO.insertReceipt("business_trip_transportation_detail", detailId, originalName, filePath);
                }
            }

            session.removeAttribute("businessTripBean");
            session.removeAttribute("receiptMap");

            request.getRequestDispatcher("/WEB-INF/views/submitSuccess.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private List<String> getFilesFromSession(HttpSession session, String stepKey) {
        Map<String, List<String>> receiptMap = (Map<String, List<String>>) session.getAttribute("receiptMap");
        return receiptMap != null ? receiptMap.getOrDefault(stepKey, new ArrayList<>()) : new ArrayList<>();
    }

    private String extractOriginalName(String filePath) {
        return filePath.substring(filePath.indexOf("_") + 1);
    }
}