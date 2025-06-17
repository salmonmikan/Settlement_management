package servlet;

import dao.ProjectDAO;
import model.Project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;

@MultipartConfig
@WebServlet(urlPatterns = {"/businessTrip", "/businessTripStep2Back", "/businessTripStep3Back","/businessTripConfirmBack"})
public class BusinessTripServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String path = request.getServletPath();
	    try {
	        List<Project> projectList = new ProjectDAO().getAllProjects();
	        request.setAttribute("projectList", projectList);
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("projectList", new ArrayList<>());
	    }

	    if ("/businessTripStep2Back".equals(path)) {
	        // step2 → back → step1
	        request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp").forward(request, response);
	    } else if ("/businessTripStep3Back".equals(path)) {
	        // step3 → back → step2
	        request.getRequestDispatcher("/WEB-INF/views/businessTrip2.jsp").forward(request, response);
	    } else if ("/businessTripConfirmBack".equals(path)) {
	        // confirm → back → step3
	        request.getRequestDispatcher("/WEB-INF/views/businessTrip3.jsp").forward(request, response);
	    } else {
	        request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp").forward(request, response);
	    }
	}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String step = request.getParameter("step");
        if (step == null) {
            request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        BusinessTripBean bean = (BusinessTripBean) session.getAttribute("businessTripBean");
        if (bean == null) {
            bean = new BusinessTripBean();
        }

        switch (step) {
            case "1":
                Step1Data s1 = new Step1Data(
                        request.getParameter("startDate"),
                        request.getParameter("endDate"),
                        request.getParameter("projectCode"),
                        request.getParameter("tripReport"),
                        request.getParameter("totalDays")
                );
                bean.setStep1Data(s1);
                session.setAttribute("businessTripBean", bean);
                request.getRequestDispatcher("/WEB-INF/views/businessTrip2.jsp").forward(request, response);
                break;

            case "2":
                String[] regionTypes = request.getParameterValues("regionType[]");
                String[] tripTypes = request.getParameterValues("tripType[]");
                String[] hotels = request.getParameterValues("hotel[]");
                String[] burdens = request.getParameterValues("burden[]");
                String[] hotelFees = request.getParameterValues("hotelFee[]");
                String[] dailyAllowances = request.getParameterValues("dailyAllowance[]");
                String[] days = request.getParameterValues("days[]");
                String[] totals = request.getParameterValues("expenseTotal[]");
                String[] memos = request.getParameterValues("memo[]");

                List<Step2Detail> step2List = new ArrayList<>();
                for (int i = 0; i < regionTypes.length; i++) {
                    Step2Detail detail = new Step2Detail(
                            regionTypes[i], tripTypes[i], hotels[i], burdens[i],
                            hotelFees[i], dailyAllowances[i], days[i], totals[i], memos[i]
                    );
                    step2List.add(detail);
                }

                bean.setStep2List(step2List);
                session.setAttribute("businessTripBean", bean);
                request.getRequestDispatcher("/WEB-INF/views/businessTrip3.jsp").forward(request, response);
                break;

            case "3":
                String[] transProjects = request.getParameterValues("transProject[]");
                String[] departures = request.getParameterValues("departure[]");
                String[] arrivals = request.getParameterValues("arrival[]");
                String[] transports = request.getParameterValues("transport[]");
                String[] fares = request.getParameterValues("fareAmount[]");
                String[] tripType3 = request.getParameterValues("transTripType[]");
                String[] burden3 = request.getParameterValues("burden[]");
                String[] totals3 = request.getParameterValues("expenseTotal[]");
                String[] memos3 = request.getParameterValues("transMemo[]");

                List<Step3Detail> step3List = new ArrayList<>();
                for (int i = 0; i < transProjects.length; i++) {
                    Step3Detail detail = new Step3Detail(
                            transProjects[i], departures[i], arrivals[i], transports[i],
                            fares[i], tripType3[i], burden3[i], totals3[i], memos3[i]
                    );
                    step3List.add(detail);
                }

                bean.setStep3List(step3List);
                session.setAttribute("businessTripBean", bean);
                request.getRequestDispatcher("/WEB-INF/views/confirm.jsp").forward(request, response);
                break;

            default:
                request.getRequestDispatcher("/WEB-INF/views/staffMenu.jsp").forward(request, response);
        }
    }
}