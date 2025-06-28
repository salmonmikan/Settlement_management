package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Đảm bảo bạn đã import RoleUtil và enum UserRole
import util.RoleUtil;
import util.RoleUtil.UserRole;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false); 

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (session == null || session.getAttribute("staffId") == null) {
            res.sendRedirect(req.getContextPath() + "/LoginServlet");
            return; 
        }

        // Lấy role từ session
        UserRole role = (UserRole) session.getAttribute("userRole");
        
        // Nếu vì lý do nào đó role không được set, gán là UNKNOWN để xử lý
        if (role == null) {
            role = UserRole.UNKNOWN;
        }

        // --- ĐOẠN SWITCH ĐÃ SỬA LỖI ---
        // Logic phân quyền dựa trên role thực tế của bạn
        switch (role) {
            case STAFF:
                // Nhân viên thường sẽ được chuyển đến menu chính của nhân viên
                req.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(req, res);
                break;

            case BUCHO:
                // Trưởng phòng sẽ được chuyển đến menu của trưởng phòng
                req.getRequestDispatcher("/WEB-INF/views/buchouMain.jsp").forward(req, res);
                break;

            case MANAGEMENT:
                // Quản lý (kế toán) sẽ được chuyển đến menu của quản lý
                req.getRequestDispatcher("/WEB-INF/views/managerMain.jsp").forward(req, res);
                break;

            case UNKNOWN:
            default:
                // Các trường hợp không xác định hoặc vai trò khác,
                // mặc định sẽ vào trang của nhân viên thường.
                req.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(req, res);
                break;
        }
    }
}