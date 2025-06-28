package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.UUID;

import bean.BusinessTripBean;
import bean.Step2Detail;
import bean.UploadedFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/businessTripStep2")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 15)
public class BusinessTripStep2Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMP_UPLOAD_DIR = "/temp_uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");

        if (trip.getStep2Details().isEmpty()) {
            trip.getStep2Details().add(new Step2Detail());
        }

        request.setAttribute("trip", trip);
        request.getRequestDispatcher("/WEB-INF/views/businessTrip/businessTrip2.jsp").forward(request, response);
    }

 // Trong file service/BusinessTripStep2Servlet.java
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        trip.getStep2Details().clear();

        // ★★★ THAY ĐỔI 1: Lấy tất cả các "Part" ra trước, chỉ một lần duy nhất ★★★
        Collection<Part> parts = request.getParts();

        String[] regionTypes = request.getParameterValues("regionType[]");
        
        if (regionTypes != null) {
            String[] tripTypes = request.getParameterValues("tripType[]");
            String[] hotels = request.getParameterValues("hotel[]");
            String[] burdens = request.getParameterValues("burden[]");
            String[] hotelFees = request.getParameterValues("hotelFee[]");
            String[] dailyAllowances = request.getParameterValues("dailyAllowance[]");
            String[] days = request.getParameterValues("days[]");
            String[] expenseTotals = request.getParameterValues("expenseTotal[]");
            String[] memos = request.getParameterValues("memo[]");

            for (int i = 0; i < regionTypes.length; i++) {
                Step2Detail detail = new Step2Detail();
                try {
                    detail.setRegionType(regionTypes[i]);
                    detail.setTripType(tripTypes[i]);
                    detail.setHotel(hotels[i]);
                    detail.setBurden(burdens[i]);
                    detail.setHotelFee(Integer.parseInt(hotelFees[i]));
                    detail.setDailyAllowance(Integer.parseInt(dailyAllowances[i]));
                    detail.setDays(Integer.parseInt(days[i]));
                    detail.setExpenseTotal(Integer.parseInt(expenseTotals[i]));
                    detail.setMemo(memos[i]);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi parse số ở Step 2, index " + i + ": " + e.getMessage());
                }

                try {
                    String fileInputName = "receipt_allowance_" + i;
                    
                    // ★★★ THAY ĐỔI 2: Lặp qua danh sách "parts" đã lấy sẵn, không gọi lại request.getParts() ★★★
                    for (Part filePart : parts) {
                        if (filePart.getName().equals(fileInputName) && filePart.getSize() > 0) {
                            String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

                            String absolutePath = getServletContext().getRealPath(TEMP_UPLOAD_DIR);
                            File uploadDir = new File(absolutePath);
                            if (!uploadDir.exists()) uploadDir.mkdirs();

                            File savedFile = new File(uploadDir, uniqueFileName);
                            try (InputStream input = filePart.getInputStream()) {
                                Files.copy(input, savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }

                            UploadedFile uploadedFile = new UploadedFile();
                            uploadedFile.setOriginalFileName(originalFileName);
                            uploadedFile.setUniqueStoredName(uniqueFileName);
                            uploadedFile.setTemporaryPath(TEMP_UPLOAD_DIR + "/" + uniqueFileName);
                            uploadedFile.setBlockIndex(i);

                            detail.getTemporaryFiles().add(uploadedFile);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                trip.getStep2Details().add(detail);
            }
        }

        session.setAttribute("trip", trip);

        String action = request.getParameter("action");
        if ("go_back".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/businessTripStep1");
        } else {
            response.sendRedirect(request.getContextPath() + "/businessTripStep3");
        }
    }
}