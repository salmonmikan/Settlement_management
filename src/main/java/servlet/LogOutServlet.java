package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logOutServlet")
public class LogOutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // clear session
        HttpSession session = request.getSession(false); 
        if (session != null) {
            session.invalidate(); 
        }

        // login gamen 
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
