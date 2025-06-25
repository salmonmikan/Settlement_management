package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import bean.BusinessTripBean.Employee;
import dao.EmployeeDAO;

@WebServlet("/employeeList")
public class EmployeeListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmployeeDAO dao = new EmployeeDAO();
        List<Employee> employeeList = dao.getAllEmployees();

        request.setAttribute("employeeList", employeeList);
        request.getRequestDispatcher("/WEB-INF/views/employeeList.jsp").forward(request, response);
    }
}
