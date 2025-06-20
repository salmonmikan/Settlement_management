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

import bean.BusinessTripBean.ReimbursementBean;
import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import dao.ProjectDAO;
import model.Project;

@MultipartConfig
@WebServlet(urlPatterns = {"/reimbursement", "/reimbursementStep2Back", "/reimbursementConfirmBack","/reimbursementStep2" })


public class ReimbursementServlet extends HttpServlet {

	private void showStep1Form(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		try {
            ProjectDAO dao = new ProjectDAO();
            List<Project> projectList = dao.getAllProjects();
            request.setAttribute("projectList", projectList);
            request.getRequestDispatcher("/WEB-INF/views/reimbursement1.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException("プロジェクト一覧の取得に失敗しました", e);
        }   
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/reimbursementStep2Back".equals(path)) {
            showStep1Form(request, response);
        } else if ("/reimbursementConfirmBack".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/reimbursement2.jsp").forward(request, response);
        } else {
            showStep1Form(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	String step = request.getParameter("step");

        if (step == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        HttpSession session = request.getSession();
        ReimbursementBean bean = (ReimbursementBean) session.getAttribute("reimbursementBean");
        if (bean == null) {
            bean = new ReimbursementBean();
        }

        switch (step) {
            case "1":
                Step1Data s1 = new Step1Data(
                        request.getParameter("date"),
                        request.getParameter("projectCode"),
                        request.getParameter("report")
                );
                System.out.println("POST received");
                System.out.println("step = " + request.getParameter("step"));
                bean.setStep1(s1);
                session.setAttribute("reimbursementBean", bean);
//                response.sendRedirect(request.getContextPath() + "/reimbursementStep2");
                request.getRequestDispatcher("/WEB-INF/views/reimbursement2.jsp").forward(request, response);
                break;

            case "2":
                String[] accountingItems = request.getParameterValues("accountingItem[]");
                String[] amounts = request.getParameterValues("amount[]");
                String[] notes = request.getParameterValues("memo[]");

                List<Step2Detail> step2List = new ArrayList<>();
                for (int i = 0; i < accountingItems.length; i++) {
                    Step2Detail detail = new Step2Detail(
                            accountingItems[i],
                            notes[i],
                            Integer.parseInt(amounts[i])
                    );
                    step2List.add(detail);
                }

                bean.setStep2List(step2List);
                session.setAttribute("reimbursementBean", bean);
                request.getRequestDispatcher("/WEB-INF/views/reimbursementConfirm.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}