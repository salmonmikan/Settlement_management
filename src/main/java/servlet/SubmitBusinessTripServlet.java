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
                appDAO.updateApplicationAmount(applicationId, totalAmount); // cần đảm bảo đã có hàm này
                tripDAO.deleteExistingTripDetails(applicationId); // bạn cần viết hàm này để xóa detail cũ

            } else {
                applicationId = appDAO.insertApplication("出張費", staffId, totalAmount);
            }

            int tripAppId = tripDAO.insertBusinessTripApplication(applicationId, bt.getStep1Data());

            Map<String, List<String>> receiptMap = (Map<String, List<String>>) session.getAttribute("receiptMap");

            // step2
            List<String> step2Files = receiptMap != null ? receiptMap.getOrDefault("step2", new ArrayList<>()) : new ArrayList<>();
            int i = 0;
            for (Step2Detail s2 : bt.getStep2List()) {
                int detailId = tripDAO.insertStep2Detail(tripAppId, s2);
                if (i < step2Files.size()) {
                    String filePath = step2Files.get(i++);
                    String originalName = filePath.substring(filePath.indexOf("_") + 1);
                    receiptDAO.insertReceipt("step2", detailId, originalName, filePath, applicationId, "step2");
                    System.out.println("📥 INSERT FILE: " + originalName + " to receipt_file, refId=" + detailId);
                }
            }

            // step3
            List<String> step3Files = receiptMap != null ? receiptMap.getOrDefault("step3", new ArrayList<>()) : new ArrayList<>();
            int j = 0;
            for (Step3Detail s3 : bt.getStep3List()) {
                int detailId = tripDAO.insertStep3Detail(tripAppId, s3);
                if (j < step3Files.size()) {
                    String filePath = step3Files.get(j++);
                    String originalName = filePath.substring(filePath.indexOf("_") + 1);
                    receiptDAO.insertReceipt("step3", detailId, originalName, filePath, applicationId, "step3");
                    System.out.println("📥 INSERT FILE: " + originalName + " to receipt_file, refId=" + detailId);
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
}