package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

     
        if (session == null || session.getAttribute("staffId") == null) {
            resp.sendRedirect(req.getContextPath() + "/WEB-INF/views/login.jsp");
            return;
        }

        
        String position = (String) session.getAttribute("position");

 
        switch (position) {
	        case "manager":
	            req.getRequestDispatcher("/WEB-INF/views/managerMain.jsp").forward(req, resp);
	            break;
	        case "staff":
	        default:
	            req.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(req, resp);
	            break;
	    }
    }
}