package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Employee;
import dao.EmployeeDAO;
import util.RoleUtil;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffId = request.getParameter("staffId");
        String password = request.getParameter("password");

        EmployeeDAO dao = new EmployeeDAO();
        Employee staff = dao.findByIdAndPassword(staffId, password);

        if (staff != null) {
            HttpSession session = request.getSession();

            // === GIỮ NGUYÊN ĐỂ KHÔNG ẢNH HƯỞNG TEAM ===
            // Lưu các thông tin riêng lẻ vào session.
            // Các thành viên khác trong team có thể đang dùng các key này.
            session.setAttribute("staffId", staff.getEmployeeId());
            session.setAttribute("staffName", staff.getFullName());
            session.setAttribute("position_id", staff.getPositionId());
            session.setAttribute("department_id", staff.getDepartmentId());

            // Xác định role và lưu vào session, cái này rất hữu ích
            RoleUtil.UserRole role = RoleUtil.detectRole(staff.getPositionId(), staff.getDepartmentId());
            session.setAttribute("userRole", role);
            
            // Ghi chú: Nên dùng Logger thay cho System.out trong dự án thực tế
            System.out.println("Login success for staffId: " + staff.getEmployeeId() + ", Role: " + role);

            request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(request, response);
        } else {
            System.out.println("Login failed - user not found or wrong password");
            request.setAttribute("error", "社員IDまたはパスワードが違います"); // ID hoặc mật khẩu không đúng
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}