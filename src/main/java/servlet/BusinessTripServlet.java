package servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
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

import bean.TransportType;
import bean.TripCategory;
import dao.ProjectDAO;
import dao1.TransportTypeDAO;
import dao1.TripCategoryDAO;
import model.Project;

@MultipartConfig
@WebServlet(urlPatterns = {"/businessTrip", "/businessTripStep2Back", "/businessTripStep3Back", "/businessTripConfirmBack"})
public class BusinessTripServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();

		try {
			List<Project> projectList = new ProjectDAO().getAllProjects();
			request.setAttribute("projectList", projectList);

			List<TripCategory> categoryList = new TripCategoryDAO().getAll();
			request.setAttribute("tripCategoryList", categoryList);

			List<TransportType> transportList = new TransportTypeDAO().getAll();
			request.setAttribute("transportList", transportList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		switch (path) {
			case "/businessTripStep2Back":
				request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp").forward(request, response);
				break;
			case "/businessTripStep3Back":
				request.getRequestDispatcher("/WEB-INF/views/businessTrip2.jsp").forward(request, response);
				break;
			case "/businessTripConfirmBack":
				request.getRequestDispatcher("/WEB-INF/views/businessTrip3.jsp").forward(request, response);
				break;
			default:
				request.getRequestDispatcher("/WEB-INF/views/businessTrip1.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String step = request.getParameter("step");
		if (step == null) {
			request.getRequestDispatcher("/menu").forward(request, response);
			return;
		}
		HttpSession session = request.getSession();

		// 領収書の保存
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

		// ステップ別処理
		switch (step) {
		case "1":
			String tripReport = request.getParameter("tripReport");
			if (tripReport != null && (
					tripReport.contains("HttpServletRequest") ||
					tripReport.contains("System.out") ||
					tripReport.contains("public class") ||
					tripReport.contains("ServletException") ||
					tripReport.contains("@Override") ||
					tripReport.contains("<script") ||
					tripReport.contains("onclick=")
			)) {
				tripReport = "※ 入力内容に不正なコードが含まれていたため、削除されました。";
			}

			LocalDate tripStartDate = LocalDate.parse(request.getParameter("startDate"));
			LocalDate tripEndDate = LocalDate.parse(request.getParameter("endDate"));
			String projectCode = request.getParameter("projectCode");

			// ✅ Tính số ngày trong Servlet
			long numOfDays = java.time.temporal.ChronoUnit.DAYS.between(tripStartDate, tripEndDate) + 1;

			session.setAttribute("tripStartDate", tripStartDate.toString());
			session.setAttribute("tripEndDate", tripEndDate.toString());
			session.setAttribute("projectCode", projectCode);
			session.setAttribute("numOfDays", numOfDays);

			List<TripCategory> categoryList = new TripCategoryDAO().getAll();
			request.setAttribute("tripCategoryList", categoryList);

			request.getRequestDispatcher("/WEB-INF/views/businessTrip2.jsp").forward(request, response);
			break;

			case "2":
				session.setAttribute("regionTypes", request.getParameterValues("regionType[]"));
				session.setAttribute("tripTypes", request.getParameterValues("tripType[]"));
				session.setAttribute("nightAllowance", request.getParameterValues("nightAllowance[]"));
				session.setAttribute("burdens", request.getParameterValues("burden[]"));
				session.setAttribute("hotelFees", request.getParameterValues("hotelFee[]"));
				session.setAttribute("dailyAllowances", request.getParameterValues("dailyAllowance[]"));
				session.setAttribute("days", request.getParameterValues("days[]"));
				session.setAttribute("totals", request.getParameterValues("expenseTotal[]"));
				session.setAttribute("memos", request.getParameterValues("memo[]"));

				List<TransportType> transportList = new TransportTypeDAO().getAll();
				request.setAttribute("transportList", transportList);
				request.getRequestDispatcher("/WEB-INF/views/businessTrip3.jsp").forward(request, response);
				break;

			case "3":
				session.setAttribute("transDates", request.getParameterValues("transDate[]"));
				session.setAttribute("departures", request.getParameterValues("departure[]"));
				session.setAttribute("arrivals", request.getParameterValues("arrival[]"));
				session.setAttribute("transports", request.getParameterValues("transport[]"));
				session.setAttribute("fareAmounts", request.getParameterValues("fareAmount[]"));
				session.setAttribute("transTripTypes", request.getParameterValues("transTripType[]"));
				session.setAttribute("burdens", request.getParameterValues("burden3[]"));
				session.setAttribute("expenseTotals", request.getParameterValues("expenseTotal[]"));
				session.setAttribute("transMemos", request.getParameterValues("transMemo[]"));

				request.setAttribute("tripStartDate", session.getAttribute("tripStartDate"));
				request.setAttribute("tripEndDate", session.getAttribute("tripEndDate"));
				request.setAttribute("projectCode", session.getAttribute("projectCode"));
				request.setAttribute("numOfDays", session.getAttribute("numOfDays"));

				request.setAttribute("regionTypes", session.getAttribute("regionTypes"));
				request.setAttribute("tripTypes", session.getAttribute("tripTypes"));
				request.setAttribute("nightAllowance", session.getAttribute("nightAllowance"));
				request.setAttribute("burdensStep2", session.getAttribute("burdens"));
				request.setAttribute("hotelFees", session.getAttribute("hotelFees"));
				request.setAttribute("dailyAllowances", session.getAttribute("dailyAllowances"));
				request.setAttribute("days", session.getAttribute("days"));
				request.setAttribute("totals", session.getAttribute("totals"));
				request.setAttribute("memos", session.getAttribute("memos"));

				request.setAttribute("transDates", session.getAttribute("transDates"));
				request.setAttribute("departures", session.getAttribute("departures"));
				request.setAttribute("arrivals", session.getAttribute("arrivals"));
				request.setAttribute("transports", session.getAttribute("transports"));
				request.setAttribute("fareAmounts", session.getAttribute("fareAmounts"));
				request.setAttribute("transTripTypes", session.getAttribute("transTripTypes"));
				request.setAttribute("burdensStep3", session.getAttribute("burdens"));
				request.setAttribute("expenseTotals", session.getAttribute("expenseTotals"));
				request.setAttribute("transMemos", session.getAttribute("transMemos"));

				request.setAttribute("receiptMap", session.getAttribute("receiptMap"));

				request.getRequestDispatcher("/WEB-INF/views/businessTripConfirm.jsp").forward(request, response);
				break;

			default:
				response.sendRedirect(request.getContextPath() + "/home");
		}
	}
}
