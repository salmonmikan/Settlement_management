package servlet;

import dao.ApplicationDAO;
import model.Application;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/applicationList")
public class ApplicationListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	List<Application> applications;
    	try {
    	    applications = new ApplicationDAO().getAllApplications();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    applications = new ArrayList<>(); // tránh NullPointerException ở JSP
    	}
        request.setAttribute("applications", applications);

        request.getRequestDispatcher("/WEB-INF/views/application_list.jsp").forward(request, response);
    }
}