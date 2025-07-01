package service;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean;
import bean.ReimbursementApplicationBean;
import bean.TransportationApplicationBean;
import bean.TransportationDetailBean;
import dao.ApplicationDAO;
import dao.BusinessTripApplicationDAO;
import dao.ReimbursementDAO;
import dao.TransportationDAO;

@WebServlet("/applicationDetail")
public class ApplicationDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            action = ""; // default処理に分岐させる
        }
        if (idParam == null || idParam.trim().isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("message", "無効な申請IDです");
            response.sendRedirect(request.getContextPath() + "/applicationMain");
            return;
        }

        try {
            int applicationId = Integer.parseInt(idParam);
            ApplicationDAO appDao = new ApplicationDAO();
            String type = appDao.getApplicationTypeById(applicationId);

            request.setAttribute("applicationId", applicationId);
            request.setAttribute("application_type", type);
            request.setAttribute("mode", "detail");
            
            if (type != null) {
                type = type.trim();
            }

            if ("出張費申請".equals(type)) {
                BusinessTripApplicationDAO dao = new BusinessTripApplicationDAO();
                BusinessTripBean bean = dao.loadBusinessTripByApplicationId(applicationId);
                request.setAttribute("trip", bean);

            } else if ("交通費".equals(type)) {
                TransportationDAO dao = new TransportationDAO();
                TransportationApplicationBean bean = dao.loadByApplicationId(applicationId);

                // 🔧 修正ポイント: 各明細の expenseTotal を再計算
                for (TransportationDetailBean detail : bean.getDetails()) {
                    int multiplier = "往復".equals(detail.getTransTripType()) ? 2 : 1;
                    if ("自己".equals(detail.getBurden())) {
                        detail.setExpenseTotal(detail.getFareAmount() * multiplier);
                    } else {
                        detail.setExpenseTotal(0);
                    }
                }
                bean.calculateTotalAmount(); // 必須: 総合計も再計算

                request.setAttribute("transportationApp", bean);

            } else if ("立替金".equals(type)) {
                ReimbursementDAO dao = new ReimbursementDAO();
                ReimbursementApplicationBean bean = dao.loadByApplicationId(applicationId);
                request.setAttribute("reimbursementApp", bean);
            }
            
            RequestDispatcher rd = null;
            switch (action) {
            case "edit":
            	// 申請一覧画面で直接編集ボタンが押された時には、詳細表示画面を飛ばして編集画面へ遷移する
            	rd = request.getRequestDispatcher("editApplication");
            	rd.forward(request, response);
            	return;
            default: // 通常はここで詳細表示画面へ遷移する
            	request.setAttribute("showSubmitButton", false);
                request.setAttribute("showEditButton", true);
                request.setAttribute("editActionUrl", "/editApplication");
                request.setAttribute("showBackButton", true);
                request.setAttribute("backActionUrl", "/applicationMain");

                request.setAttribute("view_mode", "view");
                
                rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp");
                rd.forward(request, response);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("message", "Lỗi khi tải chi tiết đơn: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/applicationMain");
        }
    }
}