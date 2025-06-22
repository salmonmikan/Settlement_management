package servlet;

import bean.BusinessTripBean.BusinessTripBean;
import dao.BusinessTripDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;
import java.io.IOException;

@WebServlet("/editBusinessTrip")
public class EditBusinessTripServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	System.out.println("🛠 appId param = " + request.getParameter("id"));
            int appId = Integer.parseInt(request.getParameter("id"));

            BusinessTripDAO dao = new BusinessTripDAO();
            BusinessTripBean trip = dao.loadBusinessTripByApplicationId(appId);

            request.setAttribute("step1Data", trip.getStep1Data());
            request.setAttribute("step2List", trip.getStep2List());
            request.setAttribute("step3List", trip.getStep3List());
            request.setAttribute("applicationId", appId);
            request.setAttribute("editMode", true); // フラグ追加

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "編集画面の読み込みに失敗しました。");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}