package servlet;

import bean.BusinessTripForm;
import bean.BusinessTripForm.AllowanceDetail;
import dao.ReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/businessTripStep2")
@MultipartConfig
public class BusinessTripStep2Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String BASE_UPLOAD_DIR = "/uploads"; // có thể sửa theo cấu hình của bạn

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String staffId = (String) session.getAttribute("staffId");

        BusinessTripForm form = (BusinessTripForm) session.getAttribute("tripForm");
        if (form == null) form = new BusinessTripForm();

        // Clear previous allowance list
        form.getAllowanceList().clear();

        String[] regionTypes = req.getParameterValues("regionType[]");
        String[] tripTypes = req.getParameterValues("tripType[]");
        String[] hotels = req.getParameterValues("hotel[]");
        String[] burdens = req.getParameterValues("burden[]");
        String[] hotelFees = req.getParameterValues("hotelFee[]");
        String[] dailyAllowances = req.getParameterValues("dailyAllowance[]");
        String[] days = req.getParameterValues("days[]");
        String[] expenseTotals = req.getParameterValues("expenseTotal[]");
        String[] memos = req.getParameterValues("memo[]");
        
        Collection<Part> parts = req.getParts();

        int blockCount = regionTypes != null ? regionTypes.length : 0;

        for (int i = 0; i < blockCount; i++) {
            AllowanceDetail detail = new AllowanceDetail();
            detail.regionType = safe(regionTypes, i);
            detail.tripType = safe(tripTypes, i);
            detail.hotel = safe(hotels, i);
            detail.burden = safe(burdens, i);
            detail.hotelFee = parseIntSafe(hotelFees, i);
            detail.dailyAllowance = parseIntSafe(dailyAllowances, i);
            detail.days = parseIntSafe(days, i);
            detail.expenseTotal = parseIntSafe(expenseTotals, i);
            detail.memo = safe(memos, i);
            
            // === Lưu file (receiptStep2_i[]) ===
            String partPrefix = "receiptStep2_" + i;
            for (Part part : parts) {
                if (part.getName().equals(partPrefix) && part.getSize() > 0) {
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();

                    String folder = getUploadFolder(req, staffId, "step2");
                    File dir = new File(folder);
                    if (!dir.exists()) dir.mkdirs();

                    String fullPath = folder + File.separator + fileName;
                    part.write(fullPath);

                    detail.receiptFileNames.add(fileName);
                }
            }

            form.getAllowanceList().add(detail);
        }

        session.setAttribute("tripForm", form);
        res.sendRedirect(req.getContextPath() + "/businessTripStep3");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        BusinessTripForm form = (BusinessTripForm) session.getAttribute("tripForm");
        if (form != null) {
            req.setAttribute("tripBean", form); // JSP debug tag sử dụng
        }
        
        req.getRequestDispatcher("/WEB-INF/views/businessTrip/businessTrip2.jsp").forward(req, res);
    }

    private String getUploadFolder(HttpServletRequest req, String staffId, String step) {
        LocalDate now = LocalDate.now();
        String folder = req.getServletContext().getRealPath(BASE_UPLOAD_DIR)
                + File.separator + now.getYear()
                + File.separator + String.format("%02d", now.getMonthValue())
                + File.separator + staffId
                + File.separator + "businessTrip"
                + File.separator + "receipt"
                + File.separator + step;
        return folder;
    }
    private void handleReceiptStep2Uploads(HttpServletRequest request, int tripApplicationId, List<Integer> detailIds) throws Exception {
        Collection<Part> parts = request.getParts();
        String staffId = (String) request.getSession().getAttribute("staffId");
        
        ReceiptDAO receiptDAO = new ReceiptDAO();
        int blockIndex = 0;

        for (int detailId : detailIds) {
            String fieldName = "receiptStep2_" + blockIndex + "[]";
            for (Part part : parts) {
                if (part.getName().equals("receiptStep2_" + blockIndex + "[]") && part.getSize() > 0) {
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    String storedFileName = staffId + "_" + System.currentTimeMillis() + "_" + fileName;
                    String uploadPath = getServletContext().getRealPath("/uploads");
                    File fileDir = new File(uploadPath);
                    if (!fileDir.exists()) fileDir.mkdirs();

                    part.write(uploadPath + File.separator + storedFileName);
                    System.out.println("📂 Found part = " + part.getName() + ", size = " + part.getSize());
                    int receiptId = receiptDAO.insertReceiptFile(fileName, "uploads/" + storedFileName);
                    receiptDAO.insertReceiptMapping(receiptId, "business_allowance_detail", String.valueOf(detailId));
                }
            }
            blockIndex++;
        }
    }
    

    private String safe(String[] arr, int i) {
        return (arr != null && arr.length > i) ? arr[i] : "";
    }

    private int parseIntSafe(String[] arr, int i) {
        try {
            return Integer.parseInt(safe(arr, i));
        } catch (Exception e) {
            return 0;
        }
    }
}