package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.DepartmentDAO;

@WebServlet("/department")
public class Department_servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

//        String position = (String) session.getAttribute("position");
        DepartmentDAO dao = new DepartmentDAO();
        session.setAttribute("department", dao.all_get());
 
        // リダイレクトで更新する
        resp.sendRedirect("department.jsp"); // 部署一覧のJSPなど
    }
}