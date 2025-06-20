package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/projectControl")
public class ProjectControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        HttpSession session = request.getSession();


        String employeeId = request.getParameter("employeeId");
        String fullName = request.getParameter("fullName");

        switch (action) {
		case "add": {
			
			
			break;
		}
		case "confirm": {
			
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + action);
		}
        
        request.getRequestDispatcher("/WEB-INF/views/projectRegister.jsp").forward(request, response);
        
//        if ("confirm".equals(action)) {
//
//            Employee emp = new Employee();
//
//            session.setAttribute("employeeTemp", emp);
//            request.getRequestDispatcher("/WEB-INF/views/employeeConfirm.jsp").forward(request, response);
//
//        } else if ("register".equals(action)) {
//            Employee emp = (Employee) session.getAttribute("employeeTemp");
//            if (emp == null) {
//                response.sendRedirect(request.getContextPath() + "/employeeRegisterPage");
//                return;
//            }
//
//            EmployeeDAO dao = new EmployeeDAO();
//            boolean result = dao.insertEmployee(emp);
//
//            if (result) {
//                session.removeAttribute("employeeTemp");
//                request.getRequestDispatcher("/WEB-INF/views/registerSuccess.jsp").forward(request, response);
//            } else {
//                request.setAttribute("error", "登録中にエラーが発生しました。");
//                request.getRequestDispatcher("/WEB-INF/views/employeeConfirm.jsp").forward(request, response);
//            }
//        }
    }
}
