// File: service/BusinessTripUpdateServlet.java (Code mới)
package service;

//... import tất cả các lớp cần thiết giống BusinessTripSubmitServlet ...
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
// ...

import bean.BusinessTripBean;
import dao.AllowanceDetailDAO;
import dao.ApplicationDAO;
import dao.BusinessTripApplicationDAO;
import dao.BusinessTripTransportationDetailDAO;
import dao.ReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DBConnection;

@WebServlet("/businessTripUpdate")
public class BusinessTripUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        int applicationId = trip.getStep1Data().getApplicationId(); // Lấy ID đã lưu
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Khởi tạo các DAO
            ApplicationDAO appDAO = new ApplicationDAO();
            BusinessTripApplicationDAO tripAppDAO = new BusinessTripApplicationDAO();
            AllowanceDetailDAO allowanceDAO = new AllowanceDetailDAO();
            BusinessTripTransportationDetailDAO transportDAO = new BusinessTripTransportationDetailDAO();
            ReceiptDAO receiptDAO = new ReceiptDAO();

            // 1. Cập nhật bảng applications chính
            trip.calculateTotalAmount();
            appDAO.updateApplication(applicationId, trip.getTotalAmount(), conn);

            // 2. Cập nhật bảng business_trip_application
            tripAppDAO.update(trip.getStep1Data(), conn);
            
            // Lấy tripApplicationId để xóa các chi tiết cũ
            int tripApplicationId = tripAppDAO.getTripApplicationIdByApplicationId(applicationId, conn); // Bạn cần thêm hàm này vào DAO
            
            // 3. Xóa tất cả chi tiết cũ (allowance, transport, receipt)
            allowanceDAO.deleteByTripApplicationId(tripApplicationId, conn);
            transportDAO.deleteByTripApplicationId(tripApplicationId, conn);
            receiptDAO.deleteByApplicationId(applicationId, conn); // Xóa file cũ

            // 4. Insert lại toàn bộ chi tiết mới (tái sử dụng code từ SubmitServlet)
            // (Bạn copy/paste toàn bộ code xử lý Step 2, Step 3, và file từ BusinessTripSubmitServlet vào đây)
            // ... Bắt đầu copy từ "Xử lý Step 2" ...
            // ...
            // ... Kết thúc copy
            
            conn.commit();
            
            // Dọn dẹp session
            session.removeAttribute("trip");
            session.removeAttribute("isEditMode");

            // Chuyển hướng về trang danh sách
            response.sendRedirect(request.getContextPath() + "/applicationMain?update_success=true");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            request.setAttribute("errorMessage", "Lỗi nghiêm trọng khi cập nhật đơn: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }
}