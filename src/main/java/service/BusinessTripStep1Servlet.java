package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean;
import bean.Project;
import bean.Step1Data;
import dao.ProjectDAO;

/**
 * 出張申請のステップ1を処理するサーブレット。
 * (Servlet xử lý Step 1 của đơn đăng ký công tác)
 */
@WebServlet("/businessTripStep1")
public class BusinessTripStep1Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Step 2から「戻る」ボタンが押された場合に呼ばれる。
     * プロジェクト一覧を取得し、Step1の画面へフォワードする。
     * (Được gọi khi nút "Back" được nhấn từ Step 2)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 「戻る」の場合、セッションデータは変更せず、単純にページを表示する
        // (Trường hợp "Back", không thay đổi dữ liệu session, chỉ đơn giản là hiển thị lại trang)
        
        // DBからプロジェクトリストを再取得する
        // (Lấy lại danh sách project từ DB)
         try {
            ProjectDAO projectDAO = new ProjectDAO();
            List<Project> projectList = projectDAO.getAllProjects();
            request.setAttribute("projectList", projectList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Step 1のJSPにフォワードする
        // (Forward đến JSP của Step 1)
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/businessTrip1.jsp").forward(request, response);
    }

    /**
     * Step 1から「次へ」ボタンが押された場合に呼ばれる。
     * 入力値をBeanに反映し、滞在日数を計算。次のステップへリダイレクト。
     * (Được gọi khi nút "Next" được nhấn từ Step 1)
     */
 // Trong file service/BusinessTripStep1Servlet.java

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        Step1Data step1Data = trip.getStep1Data();

        // 1. フォームから送信されたデータを読み込む (Đọc dữ liệu được gửi từ form)
        // Đặt tên rõ ràng là "Str" để biết đây là kiểu String
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String projectCode = request.getParameter("projectCode");
        String tripReport = request.getParameter("tripReport");

        // 2. 読み込んだデータでBeanを更新する (Cập nhật Bean với dữ liệu vừa đọc)
        step1Data.setStartDate(startDateStr);
        step1Data.setEndDate(endDateStr);
        step1Data.setProjectCode(projectCode);
        step1Data.setTripReport(tripReport);

        // 3. 日数を計算してBeanに保存する (Tính toán số ngày và lưu vào Bean)
        try {
            // Parse chuỗi ngày tháng thành đối tượng LocalDate
        	LocalDate startDate = LocalDate.parse(startDateStr.replace('/', '-'));
            LocalDate endDate = LocalDate.parse(endDateStr.replace('/', '-'));
            
            // ChronoUnit.DAYS.between tính số đêm, nên cần +1 để ra số ngày
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            
            // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
            if (daysBetween > 0) {
                step1Data.setTotalDays((int) daysBetween); // Lưu tổng số ngày vào bean
            } else {
                step1Data.setTotalDays(0); // Nếu ngày không hợp lệ thì set là 0
            }
        } catch (Exception e) {
            step1Data.setTotalDays(0); // Nếu có lỗi parse ngày thì set là 0
            System.err.println("Lỗi parse ngày ở Step 1: " + e.getMessage());
        }

        // 4. 更新されたBeanをセッションに再設定 (Đặt lại Bean đã được cập nhật vào session)
        session.setAttribute("trip", trip);

        // 5. Step 2のサーブレットにリダイレクト (Redirect đến servlet của Step 2)
        response.sendRedirect(request.getContextPath() + "/businessTripStep2");
    }
}