package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

     
        if (session == null || session.getAttribute("staffId") == null) {
            res.sendRedirect(req.getContextPath() + "/WEB-INF/views/login.jsp");
            return;
        }

        String position = (String) session.getAttribute("position");
        String department = (String) session.getAttribute("department");
        
        if(("一般社員".equals(position) || "主任".equals(position)) && "営業部".equals(department)) {
        	req.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(req, res);
        }else if("一般社員".equals(position) && "管理部".equals(department)) {
        	req.getRequestDispatcher("/WEB-INF/views/managerMain.jsp").forward(req, res);
        }else if("部長".equals(position) && "営業部".equals(department)) {
        	req.getRequestDispatcher("/WEB-INF/views/buchougamen.jsp").forward(req, res);
        }
        
    }
}