package servlet;

import bean.BusinessTripForm;
import bean.BusinessTripBean.BusinessTripBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/businessTripConfirm")
public class BusinessTripConfirmServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleConfirm(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleConfirm(request, response);
    }

    private void handleConfirm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        BusinessTripForm formData = (BusinessTripForm) session.getAttribute("tripForm");// đã sửa tên đúng chuẩn
        System.out.println("🧪 tripForm in confirm = " + formData);
        if (formData == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        BusinessTripBean tripBean = formData.getBusinessTripBean();
        if (tripBean == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Gán các attribute JSP cần
        request.setAttribute("tripBean", tripBean);
        request.setAttribute("application_type", "出張費");

        // Nếu đang edit
        String editMode = request.getParameter("editMode");
        String applicationId = request.getParameter("applicationId");
        System.out.println("✅ ConfirmServlet: tripForm exists = " + formData);
        System.out.println("🧪 businessTripBean = " + formData.getBusinessTripBean());
        if ("true".equals(editMode)) {
            request.setAttribute("editMode", true);
            request.setAttribute("applicationId", applicationId);
            request.setAttribute("application_id", applicationId); // dùng trong JSP
        }

        // Forward đến file JSP confirm
        request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp")
               .forward(request, response);
        
    }
}