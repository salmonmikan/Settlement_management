//package servlet;
//
//import java.io.IOException;
//import java.util.List;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import bean.TransportationRequest;
//import dao.TransportationRequestDAO;
//
/**
 * SubmitTransportationRequestServletは、交通費申請内容をデータベースに登録するサーブレットです。
 *
 * <p>セッションに一時保存された申請データを取り出し、まとめて登録処理を行います。</p>
 */
//@WebServlet("/submitTransportationRequest")
//public class SubmitTransportationRequestServlet extends HttpServlet {
//
/**
 * POSTリクエストを処理し、セッション内の交通費申請リストをデータベースに登録します。
 *
 * <p>
 * ・セッションから申請リストを取得<br>
 * ・1件ずつ登録処理を実行<br>
 * ・登録件数を結果画面に渡す
 * </p>
 *
 * @param request  リクエスト情報
 * @param response レスポンス情報
 * @throws ServletException サーブレットエラー時にスローされます
 * @throws IOException 入出力エラー時にスローされます
 */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        HttpSession session = request.getSession();
//        List<TransportationRequest> transList = (List<TransportationRequest>) session.getAttribute("transportationList");
//
//        if (transList == null || transList.isEmpty()) {
//            request.setAttribute("errMsg", "申請内容がありません。最初からやり直してください。");
//            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
//            return;
//        }
//
//        TransportationRequestDAO dao = new TransportationRequestDAO();
//        int successCount = 0;
//
//        for (TransportationRequest req : transList) {
//            String newId = dao.insertRequest(req);
//            if (newId != null) {
//                successCount++;
//            }
//        }
//
//        session.removeAttribute("transportationList");
//        session.removeAttribute("transportationTotal");
//
//        request.setAttribute("successCount", successCount);
//        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/transportation.jsp").forward(request, response);
//    }
//}
