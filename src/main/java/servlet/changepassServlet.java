package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PassDao;

@WebServlet("/changePassword")
public class changepassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    	throws ServletException, IOException {
    	
    	doPost(request, response);
    }	
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // 文字化け防止
        request.setCharacterEncoding("UTF-8");

        // パラメータ取得
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // ログイン中のユーザー情報をセッションから取得
        HttpSession session = request.getSession();
        String staffId = (String) session.getAttribute("staffId");

        if (staffId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 現在のパスワードが正しいか確認
        PassDao dao = new PassDao();
        boolean isValid = dao.checkPassword(staffId, currentPassword);

        if (!isValid) {
            request.setAttribute("errorMsg", "現在のパスワードが正しくありません。");
            forwardToForm(request, response);
            return;
        }

        // 新しいパスワードの確認が一致しているか
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMsg", "新しいパスワードが一致していません。");
            forwardToForm(request, response);
            return;
        }

        
        
        
        
        // パスワードを変更する
        boolean updateSuccess = dao.updatePassword(staffId, newPassword);

        if (updateSuccess) {
            // 成功したらホーム画面などへリダイレクト
            response.sendRedirect(request.getContextPath() + "/menu");
            
        } else {
            request.setAttribute("errorMsg", "パスワードの変更に失敗しました。");
            forwardToForm(request, response);
        }
    }

    private void forwardToForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/changePass.jsp");
        dispatcher.forward(request, response);
    }
}

