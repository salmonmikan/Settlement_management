package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean.TransportationRequest;
import dao.TransportationRequestDAO;

@WebServlet("/submitTransportationRequest")
public class SubmitTransportationRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<TransportationRequest> transList = (List<TransportationRequest>) session.getAttribute("transportationList");

        if (transList == null || transList.isEmpty()) {
            request.setAttribute("errMsg", "申請内容がありません。最初からやり直してください。");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        TransportationRequestDAO dao = new TransportationRequestDAO();
        int successCount = 0;

        for (TransportationRequest req : transList) {
            String newId = dao.insertRequest(req);
            if (newId != null) {
                successCount++;
            }
        }

        session.removeAttribute("transportationList");
        session.removeAttribute("transportationTotal");

        request.setAttribute("successCount", successCount);
        request.getRequestDispatcher("/WEB-INF/views/transportationRequest.jsp").forward(request, response);
    }
}
