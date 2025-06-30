package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ApplicationDAO appDAO = new ApplicationDAO();
            BusinessTripApplicationDAO tripAppDAO = new BusinessTripApplicationDAO();
            AllowanceDetailDAO allowanceDAO = new AllowanceDetailDAO();
            BusinessTripTransportationDetailDAO transportDAO = new BusinessTripTransportationDetailDAO();
            ReceiptDAO receiptDAO = new ReceiptDAO();

            trip.calculateTotalAmount();
            appDAO.updateApplication(applicationId, trip.getTotalAmount(), conn);
            tripAppDAO.update(trip.getStep1Data(), conn);
            
            int tripApplicationId = tripAppDAO.getTripApplicationIdByApplicationId(applicationId, conn);
            
            allowanceDAO.deleteByTripApplicationId(tripApplicationId, conn);
            transportDAO.deleteByTripApplicationId(tripApplicationId, conn);
            receiptDAO.deleteByApplicationId(applicationId, conn);

            for (Step2Detail detail : trip.getStep2Details()) {
                int blockId = allowanceDAO.insert(detail, tripApplicationId, conn);
                List<UploadedFile> filesForBlock = detail.getTemporaryFiles();
                for (int i = 0; i < filesForBlock.size(); i++) {
                    UploadedFile tempFile = filesForBlock.get(i);
                    moveFileToFinalLocation(tempFile, request);
                    receiptDAO.insert(applicationId, "allowance_detail", blockId, i, tempFile, staffId, conn);
                }
            }
            
            for (Step3Detail detail : trip.getStep3Details()) {
                int blockId = transportDAO.insert(detail, tripApplicationId, conn);
                List<UploadedFile> filesForBlock = detail.getTemporaryFiles();
                for (int i = 0; i < filesForBlock.size(); i++) {
                    UploadedFile tempFile = filesForBlock.get(i);
                    moveFileToFinalLocation(tempFile, request);
                    receiptDAO.insert(applicationId, "business_trip_transportation_detail", blockId, i, tempFile, staffId, conn);
                }
            }

            conn.commit();
            session.removeAttribute("trip");
            session.removeAttribute("isEditMode");

            request.setAttribute("message", "Đơn công tác phí (ID: " + applicationId + ") đã được cập nhật thành công.");
            request.setAttribute("status", "success");
            request.getRequestDispatcher("/WEB-INF/views/updateResult.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            request.setAttribute("message", "Lỗi khi cập nhật đơn: " + e.getMessage());
            request.setAttribute("status", "error");
            request.getRequestDispatcher("/WEB-INF/views/updateResult.jsp").forward(request, response);
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    private void moveFileToFinalLocation(UploadedFile tempFile, HttpServletRequest request) throws IOException {
        String realPath = request.getServletContext().getRealPath("");
        Path source = Paths.get(realPath + tempFile.getTemporaryPath());
        
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