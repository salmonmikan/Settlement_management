package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Employee;
import dao.PassDao;


@WebServlet("/changePassword")
public class ChangepassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession session = request.getSession();
        String staffId = (String) session.getAttribute("staffId");

        if (staffId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            String hashedCurrentPass = Employee.hashPassword(currentPassword);
            PassDao dao = new PassDao();
            boolean isValid = dao.checkPassword(staffId, hashedCurrentPass);

            if (!isValid) {
                request.setAttribute("errorMsg", "現在のパスワードが正しくありません。");
                forwardToForm(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("errorMsg", "新しいパスワードが一致していません。");
                forwardToForm(request, response);
                return;
            }

            String hashedNewPass = Employee.hashPassword(newPassword);
            boolean updateSuccess = dao.updatePassword(staffId, hashedNewPass);

            if (updateSuccess) {
                response.sendRedirect(request.getContextPath() + "/menu");
            } else {
                request.setAttribute("errorMsg", "パスワードの変更に失敗しました。");
                forwardToForm(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "システムエラーが発生しました。");
            forwardToForm(request, response);
        }
    }

    // 画面遷移用の共通メソッド
    private void forwardToForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/changePass.jsp").forward(request, response);
    }
}

