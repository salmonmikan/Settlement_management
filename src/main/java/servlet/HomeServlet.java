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
                resp.sendRedirect(req.getContextPath() + "/views/managerMain.jsp");
                break;
            case "staff":
            default:
                resp.sendRedirect(req.getContextPath() + "/views/staffMenu.jsp");
                break;
        }
    }
}