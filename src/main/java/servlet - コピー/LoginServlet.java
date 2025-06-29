package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean.Employee;
import dao.EmployeeDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Xử lý GET (ví dụ: người dùng mở trực tiếp Servlet bằng URL)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    // Xử lý POST (submit form đăng nhập)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffId = request.getParameter("staffId");
        String password = request.getParameter("password");
//        String hashpass = hashPassword(password);
        

        EmployeeDAO dao = new EmployeeDAO();
        Employee staff = dao.findByIdAndPassword(staffId, password);

        if (staff != null) {
            HttpSession session = request.getSession();
            session.setAttribute("staffId", staff.getEmployeeId());
            session.setAttribute("staffName", staff.getFullName()); 
            
            //画面遷移by son
            session.setAttribute("position_id", staff.getPositionId());
            session.setAttribute("department_id", staff.getDepartmentId());
            
            String position = (String) session.getAttribute("position_id");
            String department = (String) session.getAttribute("department_id");
            System.out.println("position = " + position);
            System.out.println("department = " + department);
            
            if(("P0004".equals(position) || "P0003".equals(position)) && "D0001".equals(department)) {
            	request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(request, response);
            }else if("P0004".equals(position) && "D0002".equals(department)) {
            	request.getRequestDispatcher("/WEB-INF/views/managerMain.jsp").forward(request, response);
            }else if("P0002".equals(position) && "D0001".equals(department)) {
            	request.getRequestDispatcher("/WEB-INF/views/buchouMain.jsp").forward(request, response);
            }
            
        } else {
        	System.out.println("Login failed - user not found");
            request.setAttribute("error", "IDまたはパスワードが違います");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}