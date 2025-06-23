package servlet;

import dao.ApplicationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Application;

import java.io.IOException;
import java.util.List;

@WebServlet("/approvalList")
public class ApprovalListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String department = (String) session.getAttribute("department");
        String position = (String) session.getAttribute("position");

        // Bảo vệ nếu không phải 部長
        if (!"部長".equals(position)) {
            response.sendRedirect("menu");
            return;
        }

        try {
            ApplicationDAO dao = new ApplicationDAO();
            List<Application> list = dao.getSubmittedApplicationsByDepartment(department);
            request.setAttribute("applications", list);
            request.setAttribute("position", position); // để phân biệt nút "戻る"

            request.getRequestDispatcher("/WEB-INF/views/approvalMain.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "データの取得に失敗しました。");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}