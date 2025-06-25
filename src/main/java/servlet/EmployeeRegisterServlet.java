package servlet;

import java.io.IOException;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean.Employee;
import dao.EmployeeDAO;

@WebServlet("/employeeRegisterPage")
public class EmployeeRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/employeeRegister.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        HttpSession session = request.getSession();

        // Lấy dữ liệu
        String employeeId = request.getParameter("employeeId");
        String fullName = request.getParameter("fullName");
        String furigana = request.getParameter("furigana");
        String birthDateStr = request.getParameter("birthDate");
        String address = request.getParameter("address");
        String joinDateStr = request.getParameter("joinDate");
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String departmentId = request.getParameter("departmentId");
        String positionId = request.getParameter("positionId");

        if ("confirm".equals(action)) {
            // Kiểm tra xác nhận mật khẩu
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "パスワードが一致しません。");
                request.getRequestDispatcher("/WEB-INF/views/employeeRegister.jsp").forward(request, response);
                return;
            }

            // Tạo Employee và lưu session
            Employee emp = new Employee();
            emp.setEmployeeId(employeeId);
            emp.setFullName(fullName);
            emp.setFurigana(furigana);
            emp.setBirthDate(Date.valueOf(birthDateStr));
            emp.setAddress(address);
            emp.setJoinDate(Date.valueOf(joinDateStr));
            emp.setLoginId(loginId);
            emp.setPassword(password);
            emp.setDepartmentId(departmentId);
            emp.setPositionId(positionId);

            session.setAttribute("employeeTemp", emp);
            request.getRequestDispatcher("/WEB-INF/views/employeeConfirm.jsp").forward(request, response);

        } else if ("register".equals(action)) {
            Employee emp = (Employee) session.getAttribute("employeeTemp");
            if (emp == null) {
                response.sendRedirect(request.getContextPath() + "/employeeRegisterPage");
                return;
            }

            EmployeeDAO dao = new EmployeeDAO();
            boolean result = dao.insertEmployee(emp);

            if (result) {
                session.removeAttribute("employeeTemp");
                request.getRequestDispatcher("/WEB-INF/views/registerSuccess.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "登録中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/views/employeeConfirm.jsp").forward(request, response);
            }
        }
    }
}
