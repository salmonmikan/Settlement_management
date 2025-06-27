// Filename: service/BusinessTrip/ConfirmHandler.java
package service.BusinessTrip;

import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripForm;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles the preparation of data for the final confirmation page.
 * It takes the fully populated formData from the session and forwards it to the view.
 */
public class ConfirmHandler {
    public static void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        BusinessTripForm formData = (BusinessTripForm) session.getAttribute("tripFormData");
        
        // Safety check: if session is lost, redirect to the start.
        if (formData == null || formData.getBusinessTripBean() == null) {
            response.sendRedirect(request.getContextPath() + "/businessTrip");
            return;
        }
        

        BusinessTripBean bean = formData.getBusinessTripBean();

        // Pass the entire bean to the JSP. The JSP will be responsible for displaying all data,
        // including details and their attached files.
        request.setAttribute("businessTripBean", bean);
        System.out.println("DEBUG ConfirmHandler bean = " + bean);
        // Pass other necessary attributes for the view
        request.setAttribute("application_type", "出張費");
        if (bean.isEditMode()) {
            request.setAttribute("editMode", true);
            request.setAttribute("applicationId", bean.getApplicationId());
        }

        // Forward to the confirmation page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp");
        dispatcher.forward(request, response);
    }
    
}