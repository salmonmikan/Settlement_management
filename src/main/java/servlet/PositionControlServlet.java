package servlet;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.PositionBean;
import dao.PositionDAO;

@WebServlet("/positionControl")
public class PositionControlServlet extends HttpServlet {
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
        PositionDAO dao = new PositionDAO();

        String action = request.getParameter("action");
        String id = request.getParameter("position_id");
        String name = request.getParameter("position_name");

        try {
            switch (action) {
                case "confirm_create":
                    PositionBean newBean = new PositionBean(id, name);
                    request.setAttribute("position", newBean);
                    request.setAttribute("action", "create");
                    request.getRequestDispatcher("position_confirm.jsp").forward(request, response);
                    return;

                case "confirm_update":
                    PositionBean updateCheckBean = new PositionBean(id, name);
                    request.setAttribute("position", updateCheckBean);
                    request.setAttribute("action", "update");
                    request.getRequestDispatcher("position_confirm.jsp").forward(request, response);
                    return;

                case "create":
                    PositionBean insertBean = new PositionBean(id, name);
                    if (dao.insert(insertBean)) {
                        session.setAttribute("successMsg", "登録に成功しました。");
                    } else {
                        session.setAttribute("errorMsg", "登録に失敗しました。");
                    }
                    break;

                case "update":
                    PositionBean updateBean = new PositionBean(id, name);
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
                    PositionBean editTarget = dao.findById(id);
                    request.setAttribute("position", editTarget);
                    request.getRequestDispatcher("edit_position.jsp").forward(request, response);
                    return;

                case "add":
                    response.sendRedirect("add_position.jsp");
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
        ArrayList<PositionBean> list = dao.findAll();
        session.setAttribute("position_list", list);

        // 一覧ページにリダイレクト（PRGパターン）
        response.sendRedirect("position.jsp");
    }
}
