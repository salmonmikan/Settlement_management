package service;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean;
import bean.Project; // Giả sử tên model của bạn là Project
import dao.ProjectDAO; // Giả sử tên DAO của bạn là ProjectDAO

/**
 * 出張申請プロセスの初期化を行うサーブレット。
 * (Servlet thực hiện việc khởi tạo quy trình đăng ký công tác)
 */
@WebServlet("/businessTripInit")
public class BusinessTripInitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 新しいセッションを開始し、空の出張申請Beanを作成する
        // (Bắt đầu session mới, tạo một BusinessTripBean rỗng)
        HttpSession session = request.getSession();
        BusinessTripBean trip = new BusinessTripBean();
        
        // ★ セッションに申請情報を保存する。これが各ステップで共有される。
        // (Lưu thông tin đơn vào session. Đây là đối tượng sẽ được chia sẻ qua các bước)
        session.setAttribute("trip", trip);

        // 2. DBからプロジェクトリストを取得する
        // (Lấy danh sách project từ DB)
        try {
            ProjectDAO projectDAO = new ProjectDAO();
            List<Project> projectList = projectDAO.getAllProjects();
            request.setAttribute("projectList", projectList);
        } catch (Exception e) {
            // エラーハンドリング (Xử lý lỗi)
            e.printStackTrace(); 
            // TODO: エラーページにフォワードする (Chuyển đến trang báo lỗi)
        }

        // 3. Step 1のJSPにフォワードする
        // (Forward đến JSP của Step 1)
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/businessTrip1.jsp").forward(request, response);
    }
}