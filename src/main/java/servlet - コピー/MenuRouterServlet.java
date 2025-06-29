package servlet;
import java.io.IOException;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;



import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/menu")
public class MenuRouterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String position = (String) session.getAttribute("position");
        String department = (String) session.getAttribute("department");

        String jspPath = "/WEB-INF/views/staffMenu.jsp"; // default

        if ("部長".equals(position) && "営業部".equals(department)) {
            jspPath = "/WEB-INF/views/buchouMain.jsp";
        } else if ("管理部".equals(department)) {
            jspPath = "/WEB-INF/views/managerMain.jsp";
        }

        req.getRequestDispatcher(jspPath).forward(req, res);
    }
}