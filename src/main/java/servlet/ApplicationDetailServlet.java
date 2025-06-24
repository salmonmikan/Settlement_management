package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import bean.BusinessTripBean.BusinessTripBean;
import dao.ApplicationDAO;
import dao.BusinessTripDAO;
import dao.ReceiptDAO;
import model.Application;
import model.Receipt;

@WebServlet("/applicationDetail")
public class ApplicationDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("approvalMain");
            return;
        }

        try {
            int applicationId = Integer.parseInt(idParam);

            // set cờ chi tiết mode để dùng trong JSP (để hiện btn edit, back, file...)
            request.setAttribute("isDetailMode", true);

            // lấy thông tin application
            ApplicationDAO appDao = new ApplicationDAO();
            Application application = appDao.findApplicationById(applicationId);
            if (application == null) {
                request.setAttribute("error", "申請が見つかりませんでした。");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("application_type", application.getApplicationType());
            request.setAttribute("application_id", applicationId);

            // lấy session để kiểm tra role 部長
            HttpSession session = request.getSession();
            String position = (String) session.getAttribute("position");
            if ("部長".equals(position)) {
                request.setAttribute("isApprovalScreen", true);
            }

            // lấy dữ liệu file đính kèm từ DB
            ReceiptDAO receiptDAO = new ReceiptDAO();
            Map<String, List<String>> receiptMap = receiptDAO.getReceiptsByApplicationId(applicationId);
            request.setAttribute("receiptMap", receiptMap);
            System.out.println("📦 [DEBUG] step2 files: " + receiptMap.get("step2"));
            System.out.println("📦 [DEBUG] step3 files: " + receiptMap.get("step3"));
            System.out.println("📦 [DEBUG] all keys: " + receiptMap.keySet());

            // lấy dữ liệu chi tiết tùy theo loại đơn
            if ("出張費".equals(application.getApplicationType())) {
                BusinessTripDAO btDao = new BusinessTripDAO();
                BusinessTripBean bean = btDao.findBusinessTripBean(applicationId);
                request.setAttribute("businessTripBean", bean);
                request.getRequestDispatcher("/WEB-INF/views/detail/applicationDetail.jsp").forward(request, response);
                
                
                return;
            } else if ("交通費".equals(application.getApplicationType())) {
                // TODO: tương tự như trên
            } else if ("立替金".equals(application.getApplicationType())) {
                // TODO: tương tự như trên
            } else {
                request.setAttribute("error", "不明な申請タイプです。");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "ID形式が不正です。");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "申請情報の取得中にエラーが発生しました。");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}