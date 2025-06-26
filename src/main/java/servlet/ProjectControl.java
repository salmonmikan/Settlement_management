package servlet;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ProjectDAO;
import model.ProjectList;

@WebServlet("/projectControl")
public class ProjectControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");

		HttpSession session = request.getSession();

		String[] Project_code = request.getParameterValues("Project_code");
		switch (action) {
		case "add": {
			// 登録情報入力画面のため、何もせず遷移する
			break;
		}
		case "edit": {
			ProjectDAO dao = new ProjectDAO();
			ProjectList p = dao.findByProjectCode(Project_code);

			String[] memberIds = dao.getStaffIdsByProject(p.getProject_code());
			String joinedIds = String.join(",", memberIds);
			p.setProject_members(joinedIds);

			// Lấy toàn bộ danh sách nhân viên
			Map<String, String> allStaff = dao.getStaffNamesByIds(memberIds);

			request.setAttribute("allStaff", allStaff);
			request.setAttribute("project_edit", p);

			request.getRequestDispatcher("/WEB-INF/views/projectRegister.jsp").forward(request, response);
			return;
		}

		case "delete": {
			try {
				new ProjectDAO().deleteProjects(Project_code);
				session.setAttribute("successMsg", "削除が完了しました！");
			} catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("errorMsg", "削除処理に失敗しました。");
			}
			response.sendRedirect("project_management_view");
			return;
		}

		case "confirmInsert":
		case "confirmUpdate": {
			ProjectList p = new ProjectList();
			p.setProject_code(request.getParameter("Project_code"));
			p.setProject_name(request.getParameter("Project_name"));
			p.setProject_owner(request.getParameter("Project_owner"));
			p.setStart_date(request.getParameter("Start_date"));
			p.setEnd_date(request.getParameter("End_date"));

			String startDate = request.getParameter("Start_date");
			String endDate = request.getParameter("End_date");

			String budgetStr = request.getParameter("Project_budget");
			p.setProject_budget((budgetStr == null || budgetStr.isEmpty()) ? null : Integer.parseInt(budgetStr));

			String memberStr = request.getParameter("memberIds");
			p.setProject_members(memberStr != null ? memberStr : "");

			request.setAttribute("projectConfirm", p);
			request.setAttribute("confirmMode", action.equals("confirmInsert") ? "register" : "updateFinal");

			if (memberStr != null && !memberStr.isEmpty()) {
				String[] memberIds = memberStr.split("\\s*,\\s*");
				ProjectDAO dao = new ProjectDAO();
				Map<String, String> memberMap = dao.getStaffNamesByIds(memberIds);
				request.setAttribute("memberMap", memberMap);
			}

			if (startDate != null && endDate != null && !startDate.isBlank() && !endDate.isBlank()) {
				if (startDate.compareTo(endDate) > 0) {
					request.setAttribute("errMsg", "開始日は終了日より前の日付を入力してください。");
					request.setAttribute("project_edit", p); // Trả lại dữ liệu người dùng đã nhập
					request.getRequestDispatcher("/WEB-INF/views/projectRegister.jsp").forward(request, response);
					return;
				}
			}

			request.getRequestDispatcher("/WEB-INF/views/confirm/projectConfirm.jsp").forward(request, response);
			return;
		}

		case "register": {
			try {
				ProjectList p = new ProjectList();
				p.setProject_code(request.getParameter("Project_code"));
				p.setProject_name(request.getParameter("Project_name"));
				p.setProject_owner(request.getParameter("Project_owner"));
				p.setStart_date(request.getParameter("Start_date"));
				p.setEnd_date(request.getParameter("End_date"));

				String budgetStr = request.getParameter("Project_budget");
				p.setProject_budget((budgetStr == null || budgetStr.isEmpty()) ? null : Integer.parseInt(budgetStr));

				String memberStr = request.getParameter("memberIds");
				p.setProject_members(memberStr != null ? memberStr : "");

				ProjectDAO dao = new ProjectDAO();

				// Kiểm tra nhân viên có tồn tại không
				if (memberStr != null && !memberStr.isEmpty()) {
					String[] memberIds = memberStr.split("\\s*,\\s*");
					if (!dao.areStaffIdsValid(memberIds)) {
						request.setAttribute("errorMsg", "メンバーに存在しない社員IDが含まれています。正しいIDを入力してください。");
						request.setAttribute("project_edit", p);
						request.setAttribute("screenMode", "insert");
						request.getRequestDispatcher("/WEB-INF/views/projectRegister.jsp").forward(request, response);
						return;
					}
				}

				dao.insertProject(p);
				session.setAttribute("successMsg", "登録が完了しました！");
				response.sendRedirect("project_management_view");
				return;

			} catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("errorMsg", "登録処理に失敗しました。");
				response.sendRedirect("project_management_view");
				return;
			}
		}

		case "updateFinal": {
		    try {
		        ProjectList p = new ProjectList();
		        p.setProject_code(request.getParameter("Project_code"));
		        p.setProject_name(request.getParameter("Project_name"));
		        p.setProject_owner(request.getParameter("Project_owner"));
		        p.setStart_date(request.getParameter("Start_date"));
		        p.setEnd_date(request.getParameter("End_date"));

		        String budgetStr = request.getParameter("Project_budget");
		        p.setProject_budget((budgetStr == null || budgetStr.isEmpty()) ? null : Integer.parseInt(budgetStr));

		        String memberStr = request.getParameter("memberIds");
		        p.setProject_members(memberStr != null ? memberStr : "");

		        ProjectDAO dao = new ProjectDAO();

		        // Kiểm tra nhân viên có tồn tại không
		        if (memberStr != null && !memberStr.isEmpty()) {
		            String[] memberIds = memberStr.split("\\s*,\\s*");
		            if (!dao.areStaffIdsValid(memberIds)) {
		                request.setAttribute("errorMsg", "メンバーに存在しない社員IDが含まれています。正しいIDを入力してください。");
		                request.setAttribute("project_edit", p);
		                request.setAttribute("screenMode", "edit");
		                request.getRequestDispatcher("/WEB-INF/views/projectRegister.jsp").forward(request, response);
		                return;
		            }
		        }

		        dao.updateProject(p);
		        session.setAttribute("successMsg", "更新が完了しました！");
		        response.sendRedirect("project_management_view");
		        return;

		    } catch (Exception e) {
		        e.printStackTrace();
		        session.setAttribute("errorMsg", "更新処理に失敗しました。");
		        response.sendRedirect("project_management_view");
		        return;
		    }
		}


		default:
			throw new IllegalArgumentException("ProjectControlでcaseの遷移に失敗しました");
		}

		request.getRequestDispatcher("/WEB-INF/views/projectRegister.jsp").forward(request, response);
	}
}
