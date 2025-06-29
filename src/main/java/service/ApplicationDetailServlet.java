package service;

import java.io.IOException;

import bean.BusinessTripBean;
import bean.ReimbursementApplicationBean;
import bean.TransportationApplicationBean;
import dao.ApplicationDAO;
import dao.BusinessTripApplicationDAO;
import dao.ReimbursementDAO;
import dao.TransportationDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/applicationDetail")
public class ApplicationDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            request.setAttribute("message", "申請IDが不正です。");
            request.getRequestDispatcher("/WEB-INF/views/serviceJSP/applicationMain.jsp").forward(request, response);
            return;
        }

        int applicationId = Integer.parseInt(idParam);

        try {
            ApplicationDAO appDao = new ApplicationDAO();
            String type = appDao.getApplicationTypeById(applicationId);

            request.setAttribute("application_id", applicationId);
            request.setAttribute("application_type", type);
            request.setAttribute("mode", "detail");
            
            if (type != null) {
                type = type.trim();
            }

            // SỬA LẠI CÁC CHUỖI SO SÁNH CHO ĐÚNG VỚI DATABASE
            if ("出張費申請".equals(type)) { // Đơn này vẫn giữ nguyên là "出張費申請"
                BusinessTripApplicationDAO dao = new BusinessTripApplicationDAO();
                BusinessTripBean bean = dao.loadBusinessTripByApplicationId(applicationId);
                request.setAttribute("trip", bean);

            } else if ("交通費".equals(type)) { // <<< SỬA Ở ĐÂY: Bỏ chữ "申請"
                TransportationDAO dao = new TransportationDAO();
                TransportationApplicationBean bean = dao.loadByApplicationId(applicationId);
                request.setAttribute("transportationApp", bean);

            } else if ("立替金".equals(type)) { // <<< SỬA Ở ĐÂY: Bỏ chữ "申請"
                ReimbursementDAO dao = new ReimbursementDAO();
                ReimbursementApplicationBean bean = dao.loadByApplicationId(applicationId);
                request.setAttribute("reimbursementApp", bean);
            }
            
            // Set thuộc tính cho các nút bấm
            request.setAttribute("showSubmitButton", false);
            request.setAttribute("showEditButton", true);
            request.setAttribute("editActionUrl", "/editApplication?id=" + applicationId);
            request.setAttribute("showBackButton", true);
            request.setAttribute("backActionUrl", "/applicationMain");

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            System.err.println("!!! ĐÃ XẢY RA LỖI TRONG SERVLET !!!");
            e.printStackTrace();
            request.setAttribute("message", "Lỗi khi tải chi tiết đơn: " + e.getMessage());
            RequestDispatcher rd_error = request.getRequestDispatcher("/WEB-INF/views/serviceJSP/applicationMain.jsp");
            rd_error.forward(request, response);
        }
    }
}