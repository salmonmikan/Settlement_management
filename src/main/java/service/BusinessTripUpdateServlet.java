package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean;
import bean.Step2Detail;
import bean.Step3Detail;
import bean.UploadedFile;
import dao.AllowanceDetailDAO;
import dao.ApplicationDAO;
import dao.BusinessTripApplicationDAO;
import dao.BusinessTripTransportationDetailDAO;
import dao.ReceiptDAO;
import util.DBConnection;

@WebServlet("/businessTripUpdate")
public class BusinessTripUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PERMANENT_UPLOAD_DIR = "/uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("trip") == null || session.getAttribute("staffId") == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        String staffId = (String) session.getAttribute("staffId");
        int applicationId = trip.getStep1Data().getApplicationId();
        
        Connection conn = null;
        try {
            // 1. Bắt đầu transaction
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ApplicationDAO appDAO = new ApplicationDAO();
            BusinessTripApplicationDAO tripAppDAO = new BusinessTripApplicationDAO();
            AllowanceDetailDAO allowanceDAO = new AllowanceDetailDAO();
            BusinessTripTransportationDetailDAO transportDAO = new BusinessTripTransportationDetailDAO();
            ReceiptDAO receiptDAO = new ReceiptDAO();

            // 2. Cập nhật các bảng chính
            trip.calculateTotalAmount();
            appDAO.updateApplication(applicationId, trip.getTotalAmount(), conn);
            tripAppDAO.update(trip.getStep1Data(), conn);
            
            int tripApplicationId = tripAppDAO.getTripApplicationIdByApplicationId(applicationId, conn);
            
            // 3. Xóa toàn bộ các bản ghi chi tiết và file cũ trong CSDL
            allowanceDAO.deleteByTripApplicationId(tripApplicationId, conn);
            transportDAO.deleteByTripApplicationId(tripApplicationId, conn);
            receiptDAO.deleteByApplicationId(applicationId, conn);

            // 4. Chèn lại dữ liệu chi tiết và file mới từ bean trong session
            // Xử lý Step 2
            for (Step2Detail detail : trip.getStep2Details()) {
                int blockId = allowanceDAO.insert(detail, tripApplicationId, conn);
                List<UploadedFile> filesForBlock = detail.getTemporaryFiles();
                for (int i = 0; i < filesForBlock.size(); i++) {
                    UploadedFile tempFile = filesForBlock.get(i);
                    moveFileToFinalLocation(tempFile, request);
                    receiptDAO.insert(applicationId, "allowance_detail", blockId, i, tempFile, staffId, conn);
                }
            }
            
            // Xử lý Step 3
            for (Step3Detail detail : trip.getStep3Details()) {
                int blockId = transportDAO.insert(detail, tripApplicationId, conn);
                List<UploadedFile> filesForBlock = detail.getTemporaryFiles();
                for (int i = 0; i < filesForBlock.size(); i++) {
                    UploadedFile tempFile = filesForBlock.get(i);
                    moveFileToFinalLocation(tempFile, request);
                    receiptDAO.insert(applicationId, "business_trip_transportation_detail", blockId, i, tempFile, staffId, conn);
                }
            }

            // 5. Xóa các file vật lý đã được đánh dấu xóa
            deleteMarkedFiles(trip, request);

            // 6. Mọi thứ thành công -> Commit transaction
            conn.commit();
            
            // 7. Dọn dẹp session và chuyển hướng đến trang kết quả
            session.removeAttribute("trip");
            session.removeAttribute("isEditMode");
            session.removeAttribute("filesToDelete");

            session.setAttribute("success", "出張費申請を正常に更新しました。");
            response.sendRedirect(request.getContextPath() + "/applicationMain"); 
            
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
//            request.setAttribute("message", "出張費申請の更新中にエラーが発生しました: " + e.getMessage());
//            request.setAttribute("status", "error");
//            request.getRequestDispatcher("/WEB-INF/views/serviceJSP/updateResult.jsp").forward(request, response);
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    /**
     * Di chuyển file từ thư mục tạm sang thư mục lưu trữ vĩnh viễn.
     * Cập nhật lại đường dẫn trong đối tượng UploadedFile.
     */
    private void moveFileToFinalLocation(UploadedFile tempFile, HttpServletRequest request) throws IOException {
        // Chỉ di chuyển nếu file đang nằm trong thư mục tạm
        if (tempFile.getTemporaryPath() == null || !tempFile.getTemporaryPath().startsWith("/temp_uploads")) {
            return;
        }

        String realPath = request.getServletContext().getRealPath("");
        Path source = Paths.get(realPath + tempFile.getTemporaryPath());
        
        if (Files.exists(source)) {
            Path destinationDir = Paths.get(realPath, PERMANENT_UPLOAD_DIR);
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }
            Path destination = destinationDir.resolve(tempFile.getUniqueStoredName());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            // Cập nhật lại đường dẫn thành đường dẫn vĩnh viễn
            tempFile.setTemporaryPath(PERMANENT_UPLOAD_DIR + "/" + tempFile.getUniqueStoredName());
        }
    }

    /**
     * Xóa các file vật lý đã được người dùng đánh dấu xóa.
     * Chỉ được gọi bên trong một transaction thành công.
     */
    private void deleteMarkedFiles(BusinessTripBean trip, HttpServletRequest request) {
        List<String> uniqueFileNames = trip.getFilesToDelete();
        if (uniqueFileNames == null || uniqueFileNames.isEmpty()) {
            return; // Không có gì để xóa
        }

        String realPath = request.getServletContext().getRealPath("");
        Path permanentUploadDir = Paths.get(realPath, PERMANENT_UPLOAD_DIR);

        for (String uniqueFileName : uniqueFileNames) {
            try {
                Path filePath = permanentUploadDir.resolve(uniqueFileName);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Ghi log lỗi nhưng không dừng transaction. Việc không xóa được file vật lý
                // không nên làm hỏng bản ghi DB đã cập nhật thành công.
                System.err.println("Lỗi không thể xóa file vật lý: " + uniqueFileName + " - " + e.getMessage());
            }
        }
    }
}