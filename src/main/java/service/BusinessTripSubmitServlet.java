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


@WebServlet("/businessTripSubmit")
public class BusinessTripSubmitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PERMANENT_UPLOAD_DIR = "/uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        String staffId = (String) session.getAttribute("staffId");
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

            int applicationId = appDAO.insertApplication("出張費", staffId, trip.getTotalAmount(), conn);
           

            int tripApplicationId = tripAppDAO.insert(trip.getStep1Data(), applicationId, conn);

            // Step 2
            for (Step2Detail detail : trip.getStep2Details()) {
                int blockId = allowanceDAO.insert(detail, tripApplicationId, conn);
                List<UploadedFile> filesForBlock = detail.getTemporaryFiles();
                for (int i = 0; i < filesForBlock.size(); i++) {
                    UploadedFile tempFile = filesForBlock.get(i);
                    moveFileToFinalLocation(tempFile, request);
                    receiptDAO.insert(applicationId, "allowance_detail", blockId, i, tempFile, staffId, conn);
                }
            }
            
            // Step 3
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

            request.setAttribute("applicationId", applicationId);
            session.setAttribute("success", "出張費申請が正常に送信されました。");
            response.sendRedirect(request.getContextPath() + "/applicationMain");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            
//            request.setAttribute("errorMessage", "ミス: " + e.getMessage());
//            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            session.setAttribute("errorMsg", "エラーが発生しました。再度ご確認ください。");
            response.sendRedirect(request.getContextPath() + "/businessTripSubmit");

        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    private void moveFileToFinalLocation(UploadedFile tempFile, HttpServletRequest request) throws IOException {
        String realPath = request.getServletContext().getRealPath("");
        Path source = Paths.get(realPath + tempFile.getTemporaryPath());
        
        if (Files.exists(source)) {
            Path destinationDir = Paths.get(realPath + PERMANENT_UPLOAD_DIR);
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }
            Path destination = destinationDir.resolve(tempFile.getUniqueStoredName());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            tempFile.setTemporaryPath(PERMANENT_UPLOAD_DIR + "/" + tempFile.getUniqueStoredName());
        }
    }
}