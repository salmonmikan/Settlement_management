package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.DepartmentDAO;

@WebServlet("/departmentControl")
public class DepartmentControl_servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

//        String position = (String) session.getAttribute("position");
        DepartmentDAO dao = new DepartmentDAO();
        
		String action = req.getParameter("action");
		String department_id = req.getParameter("department_id");
		String department_name = req.getParameter("department_name");
		System.out.println("ID = " + department_id);
		System.out.println("NAME = " + department_name);
		System.out.println("ACTION = " + action);
		
		switch (action) {
		case "insert": {
			dao.insert(department_id, department_name);
			break;
		}
		case "delete": {
			dao.delete(department_id);
			break;
		}
		case "update": {
			dao.update(department_id, department_name);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + action);
		}
 
		session.setAttribute("department", dao.all_get());
        RequestDispatcher rd = req.getRequestDispatcher("/department.jsp");
		rd.forward(req, resp);
    }
}