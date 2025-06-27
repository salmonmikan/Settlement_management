package servlet;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.DepartmentBean;
import dao.DepartmentDAO;

@WebServlet("/department")
public class DepartmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET対応（すべてPOSTに委譲）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    // メイン処理（POST）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        DepartmentDAO dao = new DepartmentDAO();

        String action = request.getParameter("action");
        String id = request.getParameter("department_id");
        String name = request.getParameter("department_name");

        try {
            switch (action) {
                case "confirm_create":
                    DepartmentBean newBean = new DepartmentBean(id, name);
                    request.setAttribute("department", newBean);
                    request.setAttribute("action", "create");
                    request.getRequestDispatcher("department_confirm.jsp").forward(request, response);
                    return;

                case "confirm_update":
                    DepartmentBean updateCheckBean = new DepartmentBean(id, name);
                    request.setAttribute("department", updateCheckBean);
                    request.setAttribute("action", "update");
                    request.getRequestDispatcher("department_confirm.jsp").forward(request, response);
                    return;

                case "create":
                    DepartmentBean insertBean = new DepartmentBean(id, name);
                    if (dao.insert(insertBean)) {
                        session.setAttribute("successMsg", "登録に成功しました。");
                    } else {
                        session.setAttribute("errorMsg", "登録に失敗しました。");
                    }
                    break;

                case "update":
                    DepartmentBean updateBean = new DepartmentBean(id, name);
                    if (dao.update(updateBean)) {
                        session.setAttribute("successMsg", "更新に成功しました。");
                    } else {
                        session.setAttribute("errorMsg", "更新に失敗しました。");
                    }
                    break;

                case "delete":
                    if (dao.delete(id)) {
                        session.setAttribute("successMsg", "削除に成功しました。");
                    } else {
                        session.setAttribute("errorMsg", "削除に失敗しました。");
                    }
                    break;

                case "edit":
                    DepartmentBean editTarget = dao.findById(id);
                    request.setAttribute("department", editTarget);
                    request.getRequestDispatcher("edit_department.jsp").forward(request, response);
                    return;

                case "add":
                    response.sendRedirect("add_department.jsp");
                    return;

                default:
                    session.setAttribute("errorMsg", "不正なアクションです。");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "エラーが発生しました。");
        }

        // 一覧を再取得してセッションに保存
        ArrayList<DepartmentBean> list = dao.findAll();
        session.setAttribute("department_list", list);

        // 一覧ページにリダイレクト（PRGパターン）
        response.sendRedirect("department.jsp");
    }
}
