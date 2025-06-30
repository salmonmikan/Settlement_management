package service;

import java.io.IOException;

import bean.BusinessTripBean;
import bean.ReimbursementApplicationBean;
import bean.TransportationApplicationBean;
import dao.ApplicationDAO;
import dao.BusinessTripApplicationDAO;
import dao.ReimbursementDAO;
import dao.TransportationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/editApplication")
public class EditApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        session.removeAttribute("trip");
        session.removeAttribute("reimbursement");
        session.removeAttribute("transportationApp");
        session.removeAttribute("isEditMode");

        try {
            int applicationId = Integer.parseInt(request.getParameter("id"));
            
            ApplicationDAO appDAO = new ApplicationDAO();
            String type = appDAO.getApplicationTypeById(applicationId);
            
            if (type == null) {
                 response.sendRedirect(request.getContextPath() + "/applicationMain?error=type_not_found");
                 return;
            }
            
            String targetServlet = "";
            
            type = type.trim();

            if ("出張費申請".equals(type)) {
                BusinessTripApplicationDAO dao = new BusinessTripApplicationDAO();
                BusinessTripBean bean = dao.loadBusinessTripByApplicationId(applicationId);
                bean.getStep1Data().setApplicationId(applicationId);
                session.setAttribute("trip", bean);
                targetServlet = "/businessTripStep1";
                
            } else if ("交通費".equals(type)) {
                TransportationDAO dao = new TransportationDAO();
                TransportationApplicationBean bean = dao.loadByApplicationId(applicationId);
                // Bạn cần thêm trường applicationId và getter/setter vào TransportationApplicationBean
                // bean.setApplicationId(applicationId); 
                session.setAttribute("transportationApp", bean);
                targetServlet = "/transportationRequest"; // Giả định servlet đăng ký là /transportation

            } else if ("立替金".equals(type)) {
                ReimbursementDAO dao = new ReimbursementDAO();
                ReimbursementApplicationBean bean = dao.loadByApplicationId(applicationId);
                // Bạn cần thêm trường applicationId và getter/setter vào ReimbursementApplicationBean
                bean.setApplicationId(applicationId); 
                session.setAttribute("reimbursement", bean);
                targetServlet = "/reimbursement";
            }
            
            session.setAttribute("isEditMode", true);
            response.sendRedirect(request.getContextPath() + targetServlet);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/applicationMain?error=load_failed");
        }
    }
}