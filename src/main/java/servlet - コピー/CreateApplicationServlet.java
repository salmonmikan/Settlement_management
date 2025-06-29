package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/createApplication")
public class CreateApplicationServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String type = request.getParameter("type");

    if (type == null) {
      // nếu thiếu type → quay lại menu
      RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp");
      dispatcher.forward(request, response);
      return;
    }

    switch (type) {
      case "出張費":
        // redirect để gọi BusinessTripServlet xử lý đúng logic
        response.sendRedirect(request.getContextPath() + "/businessTrip");
        break;

      default:
        response.sendRedirect(request.getContextPath() + "/staffMenu");
    }
  }
}