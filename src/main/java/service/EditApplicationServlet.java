// File: service/EditApplicationServlet.java (Code mới)
package service;

import java.io.IOException;

import bean.BusinessTripBean;
import dao.BusinessTripApplicationDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/editApplication")
public class EditApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Dọn dẹp session cũ trước khi bắt đầu
        session.removeAttribute("trip");
        session.removeAttribute("isEditMode");

        try {
            int applicationId = Integer.parseInt(request.getParameter("id"));

            // TODO: Trong tương lai, thêm logic để xác định loại đơn và gọi DAO tương ứng
            BusinessTripApplicationDAO dao = new BusinessTripApplicationDAO();
            BusinessTripBean bean = dao.loadBusinessTripByApplicationId(applicationId);

            if (bean == null) {
                response.sendRedirect(request.getContextPath() + "/applicationMain?error=not_found");
                return;
            }
            
            // Thêm application_id vào bean để dùng cho việc update sau này
            bean.getStep1Data().setApplicationId(applicationId); 

            // === BƯỚC QUAN TRỌNG ===
            // 1. Đặt bean vào SESSION với tên là "trip" để các servlet Step 1, 2, 3 có thể tái sử dụng
            session.setAttribute("trip", bean);
            
            // 2. Đặt một cờ "isEditMode" vào session để các servlet khác biết đây là luồng sửa
            session.setAttribute("isEditMode", true);

            // 3. Đặt bean vào REQUEST cũng với tên "trip" để trang JSP đầu tiên hiển thị được ngay
            request.setAttribute("trip", bean);
            
            // Chuyển hướng đến servlet của Step 1 bằng REDIRECT
            // để nó có thể load danh sách project giống như luồng đăng ký mới
            response.sendRedirect(request.getContextPath() + "/businessTripStep1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/applicationMain?error=load_failed");
        }
    }
}