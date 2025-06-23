package servlet;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.EmployeeDAO;
import model.Employee;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String loginId = request.getParameter("staffId");
        String password = request.getParameter("password");

        EmployeeDAO dao = new EmployeeDAO();
        Employee emp = dao.login(loginId, password);  // DAOでハッシュを処理した。

        if (emp != null) {
            HttpSession session = request.getSession();
            session.setAttribute("staffId", emp.getEmployeeId());
            session.setAttribute("fullName", emp.getFullName());
            session.setAttribute("position", emp.getPositionId());    
            session.setAttribute("department", emp.getDepartmentId());

            // 画面遷移（部署管理作成できたら、これは修正）
            String position = emp.getPositionId();
            String department = emp.getDepartmentId();

            if (("一般社員".equals(position) || "主任".equals(position)) && "営業部".equals(department)) {
                request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(request, response);
            } else if ("一般社員".equals(position) && "管理部".equals(department)) {
                request.getRequestDispatcher("/WEB-INF/views/managerMain.jsp").forward(request, response);
            } else if ("部長".equals(position) && "営業部".equals(department)) {
                request.getRequestDispatcher("/WEB-INF/views/buchougamen.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "未定義のロールです");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "IDまたはパスワードが違います");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
