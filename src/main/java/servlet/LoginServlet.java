package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.StaffDAO;
import model.Staff;

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
        

        StaffDAO dao = new StaffDAO();
        Staff staff = dao.findByIdAndPassword(staffId, password);

        if (staff != null) {
            HttpSession session = request.getSession();
            session.setAttribute("staffId", staff.getStaffId());
            session.setAttribute("staffName", staff.getName()); 
            
            //画面遷移by son
            session.setAttribute("position", staff.getPosition());
            session.setAttribute("department", staff.getDepartment());
            
            String position = (String) session.getAttribute("position");
            String department = (String) session.getAttribute("department");
            System.out.println("position = " + position);
            System.out.println("department = " + department);
            
            if(("一般社員".equals(position) || "主任".equals(position)) && "営業部".equals(department)) {
            	request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(request, response);
            }else if("一般社員".equals(position) && "管理部".equals(department)) {
            	request.getRequestDispatcher("/WEB-INF/views/managerMain.jsp").forward(request, response);
            }else if("部長".equals(position) && "営業部".equals(department)) {
            	request.getRequestDispatcher("/WEB-INF/views/buchouMain.jsp").forward(request, response);
            }
            
        } else {
        	System.out.println("Login failed - user not found");
            request.setAttribute("error", "IDまたはパスワードが違います");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}