package servlet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;
import dao.ProjectDAO;
import model.Project;
@MultipartConfig
@WebServlet(urlPatterns = {"/businessTrip", "/businessTripStep2Back", "/businessTripStep3Back", "/businessTripConfirmBack"})
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
        HttpSession session = request.getSession();
        BusinessTripBean bean = (BusinessTripBean) session.getAttribute("businessTripBean");
        switch (path) {
            case "/businessTripStep2Back":
                if (bean != null) {
                    request.setAttribute("step1Data", bean.getStep1Data());
                }
                request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp").forward(request, response);
                break;
            case "/businessTripStep3Back":
                if (bean != null) {
                    request.setAttribute("step1Data", bean.getStep1Data());
                    request.setAttribute("step2List", bean.getStep2List());
                }
                request.getRequestDispatcher("/WEB-INF/views/businessTrip2.jsp").forward(request, response);
                break;
            case "/businessTripConfirmBack":
                if (bean != null) {
                    request.setAttribute("step1Data", bean.getStep1Data());
                    request.setAttribute("step2List", bean.getStep2List());
                    request.setAttribute("step3List", bean.getStep3List());
                }
                request.getRequestDispatcher("/WEB-INF/views/businessTrip3.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp").forward(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String step = request.getParameter("step");
        if (step == null) {
            request.getRequestDispatcher("/menu").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        BusinessTripBean bean = (BusinessTripBean) session.getAttribute("businessTripBean");
        if (bean == null) {
            bean = new BusinessTripBean();
        }
        String uploadPath = getServletContext().getRealPath("/uploads");
        new File(uploadPath).mkdirs();
        Collection<Part> parts = request.getParts();
        List<String> step2Files = new ArrayList<>();
        List<String> step3Files = new ArrayList<>();
        for (Part part : parts) {
            String submittedFileName = part.getSubmittedFileName();
            if (submittedFileName != null && part.getSize() > 0) {
                String original = Paths.get(submittedFileName).getFileName().toString();
                String stored = UUID.randomUUID() + "_" + original;
                part.write(uploadPath + File.separator + stored);
                String partName = part.getName();
                if (partName != null && partName.startsWith("receiptStep2_")) {
                    step2Files.add("uploads/" + stored);
                } else if (partName != null && partName.startsWith("receiptStep3_")) {
                    step3Files.add("uploads/" + stored);
                }
            }
        }
        Map<String, List<String>> receiptMap = (Map<String, List<String>>) session.getAttribute("receiptMap");
        if (receiptMap == null) receiptMap = new HashMap<>();
        if (!step2Files.isEmpty()) receiptMap.put("step2", step2Files);
        if (!step3Files.isEmpty()) receiptMap.put("step3", step3Files);
        session.setAttribute("receiptMap", receiptMap);
        switch (step) {
            case "1":
                String tripReport = request.getParameter("tripReport");
                if (tripReport != null && (
                        tripReport.contains("HttpServletRequest") ||
                        tripReport.contains("System.out") ||
                        tripReport.contains("public class") ||
                        tripReport.contains("ServletException") ||
                        tripReport.contains("@Override"))
                ) {
                    tripReport = "※ 入力内容に不正なコードが含まれていたため、削除されました。";
                }
                Step1Data s1 = new Step1Data(
                        request.getParameter("startDate"),
                        request.getParameter("endDate"),
                        request.getParameter("projectCode"),
                        tripReport,
                        request.getParameter("totalDays")
                );
                bean.setStep1Data(s1);
                session.setAttribute("businessTripBean", bean);
                request.setAttribute("step1Data", bean.getStep1Data());
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
                    step2List.add(new Step2Detail(
                            regionTypes[i], tripTypes[i], hotels[i], burdens[i],
                            hotelFees[i], dailyAllowances[i], days[i], totals[i], memos[i]
                    ));
                }
                bean.setStep2List(step2List);
                session.setAttribute("businessTripBean", bean);
                request.setAttribute("step1Data", bean.getStep1Data());
                request.setAttribute("step2List", bean.getStep2List());
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
                    step3List.add(new Step3Detail(
                            transProjects[i], departures[i], arrivals[i], transports[i],
                            fares[i], tripType3[i], burden3[i], totals3[i], memos3[i]
                    ));
                }
                bean.setStep3List(step3List);
                int total2 = bean.getStep2List().stream().mapToInt(s -> Integer.parseInt(s.getExpenseTotal())).sum();
                int total3 = bean.getStep3List().stream().mapToInt(s -> Integer.parseInt(s.getTransExpenseTotal())).sum();
                bean.setTotalStep2Amount(total2);
                bean.setTotalStep3Amount(total3);
                session.setAttribute("businessTripBean", bean);
                request.setAttribute("businessTripBean", bean);
                request.setAttribute("application_type", "出張費");
                request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}