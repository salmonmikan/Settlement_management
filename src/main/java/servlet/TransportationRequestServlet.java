//package servlet;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import bean.Project;
//import bean.TransportationRequest;
//import dao.ProjectDAO;
//
/**
 * TransportationRequestServlet は、交通費申請画面の表示および申請情報の確認画面への遷移を担当するサーブレットです。
 */
//@WebServlet("/transportationRequest_ano")
//public class TransportationRequestServlet extends HttpServlet {
//
/**
 * POSTリクエストで入力された交通費申請情報を処理し、確認画面に遷移します。
 *
 * <p>複数件の申請ブロックを受け取り、セッションに保存した上で確認画面へフォワードします。</p>
 *
 * @param request  リクエストオブジェクト
 * @param response レスポンスオブジェクト
 * @throws ServletException サーブレット処理時の例外
 * @throws IOException      入出力処理時の例外
 */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            List<Project> projectList = new ProjectDAO().getAllProjects();
//            request.setAttribute("projectList", projectList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("projectList", new ArrayList<>());
//        }
//
//        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/transportation.jsp").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        HttpSession session = request.getSession();
//
//        String[] projectCodes = request.getParameterValues("projectCode[]");
//        String[] dates = request.getParameterValues("date[]");
//        String[] departures = request.getParameterValues("departure[]");
//        String[] arrivals = request.getParameterValues("arrival[]");
//        String[] transports = request.getParameterValues("transport[]");
//        String[] fares = request.getParameterValues("fareAmount[]");
//        String[] tripTypes = request.getParameterValues("transTripType[]");
//        String[] burdens = request.getParameterValues("burden[]");
//        String[] totals = request.getParameterValues("expenseTotal[]");
//        String[] memos = request.getParameterValues("transMemo[]");
//        String staffId = (String) session.getAttribute("staffId");
//
//        List<TransportationRequest> transList = new ArrayList<>();
//        int grandTotal = 0;  // Tổng tiền tất cả các block
//
//        for (int i = 0; i < dates.length; i++) {
//            String projectCode = projectCodes[i];
//            String date = dates[i];
//            String departure = departures[i];
//            String arrival = arrivals[i];
//            String transportType = transports[i];
//            String fareAmountStr = fares[i];
//            String tripType = tripTypes[i];
//            String payer = burdens[i];
//            String totalAmountStr = totals[i];
//            String memo = memos[i];
//
//            int amount = 0;
//            int totalAmount = 0;
//            try {
//                amount = (fareAmountStr != null && !fareAmountStr.isEmpty()) ? Integer.parseInt(fareAmountStr) : 0;
//                totalAmount = (totalAmountStr != null && !totalAmountStr.isEmpty()) ? Integer.parseInt(totalAmountStr) : 0;
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//
//            grandTotal += totalAmount;
//
//            TransportationRequest req = new TransportationRequest(
//                    null,              
//                    staffId,
//                    projectCode,
//                    date,
//                    departure,
//                    arrival,
//                    tripType,
//                    transportType,
//                    payer,
//                    amount,
//                    totalAmount,
//                    memo
//            );
//
//            transList.add(req);
//        }
//
//        session.setAttribute("transportationList", transList);
//        session.setAttribute("transportationTotal", grandTotal);
//
//        request.getRequestDispatcher("/WEB-INF/views/confirm/transportationRequestConfirm.jsp").forward(request, response);
//
//    }
//}
