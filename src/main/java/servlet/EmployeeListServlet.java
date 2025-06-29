package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Employee;
import dao.EmployeeDAO;

@WebServlet("/employeeList")
public class EmployeeListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    doPost(request, response);
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	session.removeAttribute("errorMsg"); // 念のため。employeeList.jspにエラー処理がある

        EmployeeDAO dao = new EmployeeDAO();
        List<Employee> employeeList = dao.getAllEmployees();

        String success = (String) session.getAttribute("success");
        session.removeAttribute("success");

        request.setAttribute("success", success);
        request.setAttribute("employeeList", employeeList);
        request.getRequestDispatcher("/WEB-INF/views/employeeList.jsp").forward(request, response);
    }
}
