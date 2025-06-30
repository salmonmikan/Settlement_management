//package servlet;
//
//import java.io.IOException;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import bean.BusinessTripBean.ReimbursementBean;
//import dao.ReimbursementDAO;
//
/**
 * SubmitReimbursementServletは、立替金申請の登録処理を担当するサーブレットです。
 *
 * <p>入力された複数の立替金情報を一括でデータベースに登録します。</p>
 */
//@WebServlet("/submitReimbursement_ano")
//public class SubmitReimbursementServlet extends HttpServlet {
//
 /**
     * POSTリクエストを処理し、立替金申請をデータベースへ登録します。
     *
     * <p>
     * ・セッションから社員IDを取得<br>
     * ・リクエストパラメータ（申請内容）を取得<br>
     * ・1件ずつReimbursementBeanに格納して登録<br>
     * ・正常／異常時に応じて遷移先やメッセージを制御
     * </p>
     *
     * @param request  リクエスト情報
     * @param response レスポンス情報
     * @throws ServletException サーブレット処理時の例外
     * @throws IOException 入出力エラー
     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        request.setCharacterEncoding("UTF-8");
//
//        HttpSession session = request.getSession();
//        String staffId = (String) session.getAttribute("staffId");
//
//        if (staffId == null) {
//            request.setAttribute("errMsg", "ログイン情報が無効です。もう一度やり直してください。");
//            request.getRequestDispatcher("/menu").forward(request, response);
//            return;
//        }
//
//        try {
//            String[] dateList = request.getParameterValues("date[]");
//            String[] destinationsList = request.getParameterValues("destinations[]");
//            String[] projectCodeList = request.getParameterValues("projectCode[]");
//            String[] reportList = request.getParameterValues("report[]");
//            String[] accountingItemList = request.getParameterValues("accountingItem[]");
//            String[] memoList = request.getParameterValues("memo[]");
//            String[] amountList = request.getParameterValues("amount[]");
//
//            if (dateList == null || destinationsList == null) {
//                request.setAttribute("errMsg", "入力内容が不正です。もう一度やり直してください。");
//                request.getRequestDispatcher("/menu").forward(request, response);
//                return;
//            }
//
//            ReimbursementDAO dao = new ReimbursementDAO();
//            int successCount = 0;
//
//            for (int i = 0; i < dateList.length; i++) {
//                ReimbursementBean bean = new ReimbursementBean();
//                bean.setStaffId(staffId);
//                bean.setDate(java.sql.Date.valueOf(dateList[i]));
//                bean.setDestinations(destinationsList[i]);
//                bean.setProjectCode(projectCodeList[i]);
//                bean.setAbstractNote(reportList[i]);
//                bean.setAccountingItem(accountingItemList[i]);
//                bean.setMemo(memoList[i]);
//                bean.setAmount(Integer.parseInt(amountList[i]));
//
//                String newId = dao.insertReimbursement(bean);
//                if (newId != null) {
//                    successCount++;
//                }
//            }
//
//            request.setAttribute("successMsg", successCount + "件の申請を登録しました。");
//            request.getRequestDispatcher("/WEB-INF/views/serviceJSP/reimbursement.jsp").forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("errMsg", "申請登録中にエラーが発生しました。");
//            request.getRequestDispatcher("/menu").forward(request, response);
//        }
//    }
//}
