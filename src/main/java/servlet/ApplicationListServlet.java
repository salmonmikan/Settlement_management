package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ApplicationDAO;
import model.Application;

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