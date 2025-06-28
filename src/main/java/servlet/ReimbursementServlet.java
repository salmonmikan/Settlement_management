package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ProjectDAO;
import model.Project;

@WebServlet("/reimbursement")
@MultipartConfig
public class ReimbursementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Project> projectList = new ProjectDAO().getAllProjects();
            request.setAttribute("projectList", projectList);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("projectList", new ArrayList<>());
        }

        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/reimbursement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String[] dateList = request.getParameterValues("date[]");
        String[] destinationsList = request.getParameterValues("destinations[]");
        String[] projectCodeList = request.getParameterValues("projectCode[]");
        String[] reportList = request.getParameterValues("report[]");
        String[] accountingItemList = request.getParameterValues("accountingItem[]");
        String[] memoList = request.getParameterValues("memo[]");
        String[] amountList = request.getParameterValues("amount[]");

        String staffId = (String) session.getAttribute("staffId");

        if (staffId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (dateList == null || destinationsList == null) {
            response.sendRedirect(request.getContextPath() + "/reimbursement");
            return;
        }

        // Gửi dữ liệu sang màn hình confirm
        request.setAttribute("dateList", dateList);
        request.setAttribute("destinationsList", destinationsList);
        request.setAttribute("projectCodeList", projectCodeList);
        request.setAttribute("reportList", reportList);
        request.setAttribute("accountingItemList", accountingItemList);
        request.setAttribute("memoList", memoList);
        request.setAttribute("amountList", amountList);

        request.getRequestDispatcher("/WEB-INF/views/confirm/reimbursementConfirm.jsp").forward(request, response);
    }
}
