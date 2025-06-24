package servlet;

import bean.BusinessTripBean.BusinessTripBean;
import dao.BusinessTripDAO;
import dao.ProjectDAO;
import dao.ReceiptDAO;
import model.Project;
import model.Receipt;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/editBusinessTrip")
public class EditBusinessTripServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            int appId = Integer.parseInt(request.getParameter("id"));

            BusinessTripDAO dao = new BusinessTripDAO();
            BusinessTripBean trip = dao.loadBusinessTripByApplicationId(appId);

            ReceiptDAO receiptDao = new ReceiptDAO();
            List<Receipt> receipts = receiptDao.getReceiptsByApplicationIdAndStep(appId, "step2");

            request.setAttribute("receipts", receipts);
            request.setAttribute("isDetailMode", true); // ✅ Bổ sung dòng này
            request.setAttribute("step1Data", trip.getStep1Data());
            request.setAttribute("step2List", trip.getStep2List());
            request.setAttribute("step3List", trip.getStep3List());
            request.setAttribute("applicationId", appId);
            request.setAttribute("editMode", true);
            session.setAttribute("businessTripBean", trip);
            session.setAttribute("step3List", trip.getStep3List());
            List<Receipt> receiptsStep3 = receiptDao.getReceiptsByApplicationIdAndStep(appId, "step3");
            session.setAttribute("step3Receipts", receiptsStep3); 
            ProjectDAO projectDao = new ProjectDAO();
            List<Project> projectList = projectDao.getAllProjects();
            request.setAttribute("projectList", projectList);


            request.setAttribute("receipts", receipts);             // dùng cho trip1 nếu cần
            session.setAttribute("step2Receipts", receipts);        // ✅ lưu vào session để trip2 dùng được

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("applicationMain.jsp");
        }
    }
}