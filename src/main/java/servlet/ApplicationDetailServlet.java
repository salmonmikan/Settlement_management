//package servlet;
//
//import bean.BusinessTripBean;
//import dao.ApplicationDAO;
//import dao.BusinessTripApplicationDAO;
//
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import jakarta.servlet.*;
//
//import java.io.IOException;
//
//@WebServlet("/applicationDetail")
//public class ApplicationDetailServlet extends HttpServlet {
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	request.setAttribute("isDetailMode", true);
//        String idParam = request.getParameter("id");
//        if (idParam == null) {
//            request.setAttribute("message", "申請IDが不正です。");
//            request.getRequestDispatcher("/applicationMain.jsp").forward(request, response);
//            return;
//        }
//
//        int applicationId = Integer.parseInt(idParam);
//        try {
//            ApplicationDAO appDao = new ApplicationDAO();
//            String type = appDao.getApplicationTypeById(applicationId);
//            request.setAttribute("application_id", applicationId);
//            request.setAttribute("application_type", type);
//            request.setAttribute("mode", "detail");
//
//            if ("出張費".equals(type)) {
//                BusinessTripApplicationDAO dao = new BusinessTripApplicationDAO();
//                BusinessTripBean bean = dao.loadBusinessTripByApplicationId(applicationId);
//                
//                request.setAttribute("businessTripBean", bean); // dùng cho confirm.jsp
//                request.getSession().setAttribute("businessTripBean", bean); // dùng cho JSP step1/2/3
//                
//                request.setAttribute("isDetailMode", true);
//            }
//            // TODO: xử lý cho 交通費 và 立替金 nếu cần
//
//            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp");
//            rd.forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("message", "詳細情報の取得中にエラーが発生しました。");
//            request.getRequestDispatcher("/applicationMain.jsp").forward(request, response);
//        }
//    }
//}