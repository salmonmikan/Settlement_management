package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/manage")
public class managerMain extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        String position = (String) session.getAttribute("position");

 
//        switch (position) {
//            case "manager":
//                resp.sendRedirect(req.getContextPath() + "/views/managerMain.jsp");
//                break;
//            case "staff":
//            default:
//                resp.sendRedirect(req.getContextPath() + "/views/staffMenu.jsp");
//                break;
//        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/managerMain.jsp");
		rd.forward(req, resp);
    }
}