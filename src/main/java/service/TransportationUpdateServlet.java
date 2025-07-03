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

import bean.TransportationApplicationBean;
import bean.TransportationDetailBean;
import bean.UploadedFile;
import dao.ApplicationDAO;
import dao.ReceiptDAO;
import dao.TransportationDAO;
import util.DBConnection;

@WebServlet("/transportationUpdate")
public class TransportationUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PERMANENT_UPLOAD_DIR = "/uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("transportationApp") == null || session.getAttribute("staffId") == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        TransportationApplicationBean transportationApp = (TransportationApplicationBean) session.getAttribute("transportationApp");
        String staffId = (String) session.getAttribute("staffId");
        int applicationId = transportationApp.getApplicationId();

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ApplicationDAO appDAO = new ApplicationDAO();
            TransportationDAO transportationDAO = new TransportationDAO();
            ReceiptDAO receiptDAO = new ReceiptDAO();

            transportationApp.calculateTotalAmount();
            appDAO.updateApplication(applicationId, transportationApp.getTotalAmount(), conn);

            transportationDAO.deleteByApplicationId(applicationId, conn);
            receiptDAO.deleteByApplicationIdAndBlockType(applicationId, "transportation_request", conn);
            
            for (TransportationDetailBean detail : transportationApp.getDetails()) {
                int blockId = transportationDAO.insert(detail, applicationId, conn);
                List<UploadedFile> filesForBlock = detail.getTemporaryFiles();
                for (int i = 0; i < filesForBlock.size(); i++) {
                    UploadedFile tempFile = filesForBlock.get(i);
                    moveFileToFinalLocation(tempFile, request);
                    receiptDAO.insert(applicationId, "transportation_request", blockId, i, tempFile, staffId, conn);
                }
            }
            
            conn.commit();
            session.removeAttribute("transportationApp");
            session.removeAttribute("isEditMode");

            session.setAttribute("success", "交通費申請を正常に更新しました。");
            response.sendRedirect(request.getContextPath() + "/applicationMain");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            
            session.setAttribute("errorMsg", "更新中にエラーが発生しました: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/transportationSubmit");
//             request.setAttribute("message", "出張費申請の更新中にエラーが発生しました: " + e.getMessage());
//             request.setAttribute("status", "error");
//             request.getRequestDispatcher("/WEB-INF/views/serviceJSP//updateResult.jsp").forward(request, response);
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    private void moveFileToFinalLocation(UploadedFile tempFile, HttpServletRequest request) throws IOException {
        String realPath = request.getServletContext().getRealPath("");
        Path source = Paths.get(realPath, tempFile.getTemporaryPath());
        
        if (Files.exists(source)) {
            Path destinationDir = Paths.get(realPath, PERMANENT_UPLOAD_DIR);
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }
            Path destination = destinationDir.resolve(tempFile.getUniqueStoredName());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            tempFile.setTemporaryPath(PERMANENT_UPLOAD_DIR + "/" + tempFile.getUniqueStoredName());
        }
    }
}
