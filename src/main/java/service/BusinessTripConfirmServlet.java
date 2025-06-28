package service;

import java.io.IOException;
import bean.BusinessTripBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 申請内容の確認ページを表示するためのサーブレット。
 * (Servlet để hiển thị trang xác nhận nội dung đơn)
 */
@WebServlet("/businessTripConfirm")
public class BusinessTripConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        // 1. セッションから申請情報を取得 (Lấy thông tin đơn từ session)
        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");

        // 2. 表示用のデータを計算・準備 (Tính toán, chuẩn bị dữ liệu để hiển thị)
        // Ví dụ: tính tổng số tiền. Việc này nên được thực hiện trong Bean hoặc ở đây.
        trip.calculateTotalAmount(); // Chúng ta sẽ thêm phương thức này vào BusinessTripBean

        // 3. 確認ページの表示モードを設定 (Thiết lập chế độ hiển thị cho trang confirm)
        // Đây là ví dụ cho trường hợp "tạo đơn mới".
        request.setAttribute("application_type", "出張費"); // Loại đơn
        request.setAttribute("showBackButton", true);
        request.setAttribute("backActionUrl", "/businessTripStep3"); // URL của nút Back
        request.setAttribute("showSubmitButton", true);
        request.setAttribute("submitActionUrl", "/businessTripSubmit"); // URL của nút Gửi đơn

        // 4. JSPで表示するために、リクエストスコープに申請情報を設定
        // (Đặt thông tin đơn vào request scope để JSP hiển thị)
        request.setAttribute("trip", trip);

        // 5. 共通の確認ページテンプレートにフォワード
        // (Forward đến template trang confirm chung)
        request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp").forward(request, response);
    }
}