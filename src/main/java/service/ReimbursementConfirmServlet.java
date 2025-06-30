package service;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.ReimbursementApplicationBean;

/**
 * 立替金申請の確認画面を表示するためのサーブレット。
 * セッションに保持された申請情報を使用し、確認ページに必要な情報をリクエストスコープに設定する。
 */
@WebServlet("/reimbursementConfirm")
public class ReimbursementConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 立替金申請の確認画面を表示する。
     * セッションに格納された ReimbursementApplicationBean をもとに、
     * 申請内容を確認ページに渡す。セッションが無効、または申請情報が存在しない場合は、
     * 初期化処理（`/reimbursementInit`）へリダイレクトする。
     *
     * @param request  HTTPリクエスト（セッションからの申請情報取得用）
     * @param response HTTPレスポンス（確認画面または初期化画面へ遷移）
     * @throws ServletException サーブレットの処理エラー
     * @throws IOException 入出力エラー
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("reimbursement") == null) {
            response.sendRedirect(request.getContextPath() + "/reimbursementInit");
            return;
        }

        ReimbursementApplicationBean reimbursement = (ReimbursementApplicationBean) session.getAttribute("reimbursement");
        reimbursement.calculateTotalAmount();

        Boolean isEditMode = (Boolean) session.getAttribute("isEditMode");
        if (isEditMode == null) {
            isEditMode = false;
        }

        request.setAttribute("application_type", "立替金");
        request.setAttribute("reimbursementApp", reimbursement); 

        if (isEditMode) {
            request.setAttribute("isEditMode", true);
            request.setAttribute("showBackButton", true);
            request.setAttribute("backActionUrl", "/reimbursement");
            request.setAttribute("showSubmitButton", true);
            request.setAttribute("submitActionUrl", "/reimbursementUpdate");
        } else {
            request.setAttribute("isEditMode", false);
            request.setAttribute("showBackButton", true);
            request.setAttribute("backActionUrl", "/reimbursement");
            request.setAttribute("showSubmitButton", true);
            request.setAttribute("submitActionUrl", "/reimbursementSubmit");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp");
        rd.forward(request, response);
    }
}