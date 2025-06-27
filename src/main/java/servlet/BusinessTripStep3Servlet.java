package servlet;

import bean.BusinessTripForm;
import bean.BusinessTripForm.TransportDetail;
import dao.ReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/businessTripStep3")
@MultipartConfig
public class BusinessTripStep3Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String BASE_UPLOAD_DIR = "/uploads";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        String staffId = (String) session.getAttribute("staffId");

        BusinessTripForm form = (BusinessTripForm) session.getAttribute("tripForm");
        if (form == null) form = new BusinessTripForm();

        form.getTransportList().clear();

        // Lấy danh sách field
        String[] transProjects = req.getParameterValues("transProject[]");
        String[] departures = req.getParameterValues("departure[]");
        String[] arrivals = req.getParameterValues("arrival[]");
        String[] transports = req.getParameterValues("transport[]");
        String[] fareAmounts = req.getParameterValues("fareAmount[]");
        String[] transTripTypes = req.getParameterValues("transTripType[]");
        String[] burdens = req.getParameterValues("burden[]");
        String[] expenseTotals = req.getParameterValues("expenseTotal[]");
        String[] memos = req.getParameterValues("transMemo[]");

        Collection<Part> parts = req.getParts();

        int blockCount = transProjects != null ? transProjects.length : 0;

        for (int i = 0; i < blockCount; i++) {
            TransportDetail detail = new TransportDetail();
            detail.transProject = safe(transProjects, i);
            detail.departure = safe(departures, i);
            detail.arrival = safe(arrivals, i);
            detail.transport = safe(transports, i);
            detail.fareAmount = parseIntSafe(fareAmounts, i);
            detail.transTripType = safe(transTripTypes, i);
            detail.burden = safe(burdens, i);
            detail.expenseTotal = parseIntSafe(expenseTotals, i);
            detail.memo = safe(memos, i);

            // === Xử lý file upload theo block: receiptStep3_i[] ===
            String partPrefix = "receiptStep3_" + i;
            for (Part part : parts) {
                if (part.getName().equals(partPrefix) && part.getSize() > 0) {
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();

                    String folder = getUploadFolder(req, staffId, "step3");
                    File dir = new File(folder);
                    if (!dir.exists()) dir.mkdirs();

                    String fullPath = folder + File.separator + fileName;
                    part.write(fullPath);

                    detail.receiptFileNames.add(fileName);
                }
            }

            form.getTransportList().add(detail);
        }
        form.buildBusinessTripBean(); 
        session.setAttribute("tripForm", form);

        // ✅ Sau khi hoàn tất step 3 → chuyển sang confirm
        res.sendRedirect(req.getContextPath() + "/businessTripConfirm");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        BusinessTripForm form = (BusinessTripForm) session.getAttribute("tripForm");

        if (form != null) {
            req.setAttribute("tripBean", form);
        }

        req.getRequestDispatcher("/WEB-INF/views/businessTrip/businessTrip3.jsp").forward(req, res);
    }
    private void handleReceiptStep3Uploads(HttpServletRequest request, int tripApplicationId, List<Integer> detailIds) throws Exception {
        Collection<Part> parts = request.getParts();
        String staffId = (String) request.getSession().getAttribute("staffId");

        ReceiptDAO receiptDAO = new ReceiptDAO();
        int blockIndex = 0;

        for (int detailId : detailIds) {
            String fieldName = "receiptStep3_" + blockIndex + "[]";
            for (Part part : parts) {
                if (part.getName().equals("receiptStep3_" + blockIndex + "[]") && part.getSize() > 0) {
                    String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    String storedFileName = staffId + "_" + System.currentTimeMillis() + "_" + fileName;
                    String uploadPath = getServletContext().getRealPath("/uploads");
                    File fileDir = new File(uploadPath);
                    if (!fileDir.exists()) fileDir.mkdirs();

                    part.write(uploadPath + File.separator + storedFileName);
                    System.out.println("📂 Found part = " + part.getName() + ", size = " + part.getSize());
                    int receiptId = receiptDAO.insertReceiptFile(fileName, "uploads/" + storedFileName);
                    receiptDAO.insertReceiptMapping(receiptId, "business_transportation_detail", String.valueOf(detailId));
                }
            }
            blockIndex++;
        }
    }

    private String getUploadFolder(HttpServletRequest req, String staffId, String step) {
        LocalDate now = LocalDate.now();
        return req.getServletContext().getRealPath(BASE_UPLOAD_DIR)
                + File.separator + now.getYear()
                + File.separator + String.format("%02d", now.getMonthValue())
                + File.separator + staffId
                + File.separator + "businessTrip"
                + File.separator + "receipt"
                + File.separator + step;
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