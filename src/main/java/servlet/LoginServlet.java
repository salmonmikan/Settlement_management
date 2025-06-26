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
            session.setAttribute("staffId", staff.getEmployeeId());
            session.setAttribute("staffName", staff.getFullName());

            String position = staff.getPositionId();
            String department = staff.getDepartmentId();

            session.setAttribute("position_id", position);
            session.setAttribute("department_id", department);

            // Xác định role và lưu vào session
            RoleUtil.UserRole role = RoleUtil.detectRole(position, department);
            session.setAttribute("userRole", role);

            System.out.println("position = " + position);
            System.out.println("department = " + department);
            System.out.println("role = " + role);

            request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(request, response);
        } else {
            System.out.println("Login failed - user not found");
            request.setAttribute("error", "IDまたはパスワードが違います");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}