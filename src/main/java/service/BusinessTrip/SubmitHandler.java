// Filename: service/BusinessTrip/SubmitHandler.java
package service.BusinessTrip;

import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;
import bean.BusinessTripFormData;
import bean.UploadedFile;
import dao.ApplicationHeaderDAO;
import dao.BusinessTripDAO;
import dao.ReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DBConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handles the final submission of the Business Trip application.
 * All database operations are wrapped in a single transaction.
 * Implements a "smart update" logic to prevent data loss.
 */
public class SubmitHandler {

    private static final String PERMANENT_UPLOAD_DIR = "secure_uploads";

    public static void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        BusinessTripFormData formData = (BusinessTripFormData) session.getAttribute("tripFormData");

        if (formData == null || formData.getBusinessTripBean() == null) {
            response.sendRedirect(request.getContextPath() + "/businessTrip");
            return;
        }

        BusinessTripBean bean = formData.getBusinessTripBean();
        // Giả sử staffId được lưu trong session sau khi đăng nhập
        String staffId = (String) session.getAttribute("staffId"); 
        if (staffId == null) staffId = "STF001"; // Giá trị mặc định để test

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

            if (bean.isEditMode()) {
                handleUpdate(request, conn, bean, staffId);
            } else {
                handleCreate(request, conn, bean, staffId);
            }

            conn.commit(); // KẾT THÚC TRANSACTION THÀNH CÔNG (COMMIT)

            session.removeAttribute("tripFormData");
            response.sendRedirect(request.getContextPath() + "/submitSuccess.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // GẶP LỖI, QUAY LÙI (ROLLBACK)
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            request.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình gửi đơn. Vui lòng thử lại. Lỗi: " + e.getMessage());
            request.setAttribute("businessTripBean", bean); // Gửi lại bean để hiển thị lại form
            request.getRequestDispatcher("/WEB-INF/views/confirm/applicationConfirm.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    private static void handleCreate(HttpServletRequest request, Connection conn, BusinessTripBean bean, String staffId) throws SQLException, IOException {
        ApplicationHeaderDAO headerDAO = new ApplicationHeaderDAO(conn);
        BusinessTripDAO tripDAO = new BusinessTripDAO(conn);
        ReceiptDAO receiptDAO = new ReceiptDAO(conn);

        // 1. Insert Header
        int applicationId = headerDAO.insertHeader(staffId, "business_trip", bean.getTotalAmount());
        
        // 2. Insert Business Trip Application
        int tripApplicationId = tripDAO.insertBusinessTripApplication(applicationId, bean.getStep1Data());

        // 3. Insert Step 2 Details and their files
        for (Step2Detail detail : bean.getStep2List()) {
            int allowanceDetailId = tripDAO.insertStep2Detail(tripApplicationId, detail);
            saveUploadedFilesInDetail(request, receiptDAO, "allowance_detail", allowanceDetailId, applicationId, detail.getAttachedFiles());
        }

        // 4. Insert Step 3 Details and their files
        for (Step3Detail detail : bean.getStep3List()) {
            int transDetailId = tripDAO.insertStep3Detail(tripApplicationId, detail);
            saveUploadedFilesInDetail(request, receiptDAO, "business_trip_transportation_detail", transDetailId, applicationId, detail.getAttachedFiles());
        }
    }

    private static void handleUpdate(HttpServletRequest request, Connection conn, BusinessTripBean bean, String staffId) throws SQLException, IOException {
        ApplicationHeaderDAO headerDAO = new ApplicationHeaderDAO(conn);
        BusinessTripDAO tripDAO = new BusinessTripDAO(conn);
        ReceiptDAO receiptDAO = new ReceiptDAO(conn);
        int applicationId = bean.getApplicationId();
        int tripApplicationId = bean.getTripApplicationId();
        
        // 1. Update Header
        headerDAO.updateHeader(applicationId, bean.getTotalAmount());
        
        // 2. Update Business Trip Application
        tripDAO.updateBusinessTripApplication(applicationId, bean.getStep1Data());
        
        // 3. Smart update for Step 2
        updateDetails(request, conn, tripDAO, receiptDAO,
            bean.getStep2List(),
            tripDAO.findBusinessTripBean(applicationId).getStep2List(),
            detail -> {
                try {
                    return tripDAO.insertStep2Detail(tripApplicationId, (Step2Detail) detail);
                } catch (SQLException e) { throw new RuntimeException(e); }
            },
            detail -> { /* TODO: Implement updateStep2Detail in DAO if needed */ },
            "allowance_detail", applicationId);

        // 4. Smart update for Step 3
        updateDetails(request, conn, tripDAO, receiptDAO,
            bean.getStep3List(),
            tripDAO.findBusinessTripBean(applicationId).getStep3List(),
            detail -> {
                try {
                    return tripDAO.insertStep3Detail(tripApplicationId, (Step3Detail) detail);
                } catch (SQLException e) { throw new RuntimeException(e); }
            },
            detail -> { /* TODO: Implement updateStep3Detail in DAO if needed */ },
            "business_trip_transportation_detail", applicationId);
    }
    
    // Generic method for smart update logic
    private static <T> void updateDetails(HttpServletRequest request, Connection conn, BusinessTripDAO tripDAO, ReceiptDAO receiptDAO,
                                        List<T> formDetails, List<T> dbDetails,
                                        Function<T, Integer> insertFunction,
                                        java.util.function.Consumer<T> updateFunction,
                                        String refTable, int applicationId) throws SQLException, IOException {

        Function<T, Integer> getRefId = detail -> (detail instanceof Step2Detail) ? ((Step2Detail)detail).getRefId() : ((Step3Detail)detail).getRefId();
        Function<T, List<UploadedFile>> getFiles = detail -> (detail instanceof Step2Detail) ? ((Step2Detail)detail).getAttachedFiles() : ((Step3Detail)detail).getAttachedFiles();

        Map<Integer, T> dbMap = dbDetails.stream().collect(Collectors.toMap(getRefId, Function.identity()));
        Set<Integer> formIds = formDetails.stream().map(getRefId).collect(Collectors.toSet());

        // DELETE details that are in DB but not in form
        for (T dbDetail : dbDetails) {
            if (!formIds.contains(getRefId.apply(dbDetail))) {
                // Delete associated receipts first
                List<Integer> receiptIdsToDelete = getFiles.apply(dbDetail).stream()
                    .map(f -> {
                        try {
                            // This part is tricky, need a way to get receipt_id from UploadedFile
                            // For simplicity, we assume all files are re-synced on update.
                            return 0; // Placeholder
                        } catch (Exception e) { return 0; }
                    }).collect(Collectors.toList());
                List<String> pathsToDelete = receiptDAO.deleteReceiptsByIds(receiptIdsToDelete);
                deletePhysicalFiles(request, pathsToDelete);
                // TODO: tripDAO.deleteDetailById(getRefId.apply(dbDetail));
            }
        }
        
        // INSERT new details and UPDATE existing ones
        for(T formDetail : formDetails) {
            if (getRefId.apply(formDetail) == 0) { // New detail
                int newDetailId = insertFunction.apply(formDetail);
                saveUploadedFilesInDetail(request, receiptDAO, refTable, newDetailId, applicationId, getFiles.apply(formDetail));
            } else { // Existing detail
                // updateFunction.accept(formDetail);
                // For simplicity, we can delete old files and re-insert new ones for the updated detail
                // TODO: A more refined approach would compare file lists.
                // For now, let's assume the "delete-all-re-insert" for files *per detail* is acceptable.
                // receiptDAO.deleteAllReceiptsByRefId(getRefId.apply(formDetail)); // New DAO method needed
                saveUploadedFilesInDetail(request, receiptDAO, refTable, getRefId.apply(formDetail), applicationId, getFiles.apply(formDetail));
            }
        }
    }

    private static void saveUploadedFilesInDetail(HttpServletRequest request, ReceiptDAO receiptDAO, String refTable, int refId, int applicationId, List<UploadedFile> files) throws IOException, SQLException {
        if (files == null) return;
        for (UploadedFile file : files) {
            String permanentPath = moveTempFileToPermanent(request, file.getTempStoredPath(), applicationId);
            receiptDAO.insertReceipt(refTable, refId, file.getOriginalFileName(), permanentPath, applicationId, "business_trip");
        }
    }

    private static String moveTempFileToPermanent(HttpServletRequest request, String tempRelativePath, int applicationId) throws IOException {
        if (tempRelativePath == null || tempRelativePath.isEmpty()) return null;
        
        String realPathRoot = request.getServletContext().getRealPath("/");
        Path tempPath = Paths.get(realPathRoot, tempRelativePath);
        
        if (Files.exists(tempPath)) {
            String permanentDirStr = PERMANENT_UPLOAD_DIR + File.separator + applicationId;
            File permanentDir = new File(realPathRoot + permanentDirStr);
            if (!permanentDir.exists()) permanentDir.mkdirs();
            
            Path permanentPath = Paths.get(permanentDir.getAbsolutePath(), tempPath.getFileName().toString());
            Files.move(tempPath, permanentPath, StandardCopyOption.REPLACE_EXISTING);
            
            return permanentDirStr + "/" + tempPath.getFileName().toString(); // Return new relative path
        }
        return tempRelativePath; // Return old path if it couldn't be moved (maybe already permanent)
    }

    private static void deletePhysicalFiles(HttpServletRequest request, List<String> relativePaths) {
        String realPathRoot = request.getServletContext().getRealPath("/");
        for (String path : relativePaths) {
            if (path == null || path.isEmpty()) continue;
            try {
                Files.deleteIfExists(Paths.get(realPathRoot, path));
            } catch (IOException e) {
                e.printStackTrace(); // Log error but don't stop the whole transaction
            }
        }
    }
}