package servlet;

import dao.StaffDAO;
import model.Staff;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Xử lý GET (ví dụ: người dùng mở trực tiếp Servlet bằng URL)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("views/login.jsp"); // hoặc "login.jsp" nếu login.jsp đặt trực tiếp trong webapp/
    }

    // Xử lý POST (submit form đăng nhập)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffId = request.getParameter("staffId");
        String password = request.getParameter("password");

        StaffDAO dao = new StaffDAO();
        Staff staff = dao.findByIdAndPassword(staffId, password);

        if (staff != null) {
            HttpSession session = request.getSession();
            session.setAttribute("staffId", staff.getStaffId());
            session.setAttribute("staffName", staff.getName()); 
            session.setAttribute("position", staff.getPosition());

            response.sendRedirect(request.getContextPath() + "/views/staffMenu.jsp");
        } else {
        	System.out.println("Login failed - user not found");
            request.setAttribute("error", "IDまたはパスワードが違います");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}