package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import bean.BusinessTripBean;
import bean.Step2Detail;
import bean.UploadedFile;


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
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/businessTrip2.jsp").forward(request, response);
    }

 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.getParts();
    	request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        String action = request.getParameter("action");
        if ("go_back".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/businessTripStep1");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");

        // 1. 消えたボタンで消えたファイルの処理 (×)
        String filesToDeleteParam = request.getParameter("filesToDelete");
        if (filesToDeleteParam != null && !filesToDeleteParam.isEmpty()) {
            List<String> filesToDeleteList = List.of(filesToDeleteParam.split(","));
         // 1. Ghi nhận các file cần xóa vào bean session
            trip.getFilesToDelete().addAll(filesToDeleteList);

//            String realPath = getServletContext().getRealPath("");
         // 2. Chỉ xóa tham chiếu file khỏi danh sách chi tiết, KHÔNG xóa file vật lý
            for (Step2Detail detail : trip.getStep2Details()) {
                detail.getTemporaryFiles().removeIf(file -> filesToDeleteList.contains(file.getUniqueStoredName()));
            }
        }

        // 2. フォームからデータを取得
        String[] regionTypes = request.getParameterValues("regionType[]");
        List<Step2Detail> detailsInSession = trip.getStep2Details();
        int numSubmittedBlocks = (regionTypes != null) ? regionTypes.length : 0;
        
        // 3. 更新またはブロックを追加
        String[] tripTypes = request.getParameterValues("tripType[]");
        String[] hotels = request.getParameterValues("hotel[]");
        String[] burdens = request.getParameterValues("burden[]");
        String[] hotelFees = request.getParameterValues("hotelFee[]");
        String[] dailyAllowances = request.getParameterValues("dailyAllowance[]");
        String[] days = request.getParameterValues("days[]");
        String[] expenseTotals = request.getParameterValues("expenseTotal[]");
        String[] memos = request.getParameterValues("memo[]");

        for (int i = 0; i < numSubmittedBlocks; i++) {
            Step2Detail detail;
            if (i < detailsInSession.size()) {
                // sessionに存在しているブロックは更新
                detail = detailsInSession.get(i);
            } else {
                // 追加 ('+') 
                detail = new Step2Detail();
                detailsInSession.add(detail);
            }
            
            // フォームの属性を更新する
            detail.setRegionType(regionTypes[i]);
            detail.setTripType(tripTypes[i]);
            detail.setHotel(hotels[i]);
            detail.setBurden(burdens[i]);
            detail.setHotelFee(Integer.parseInt(hotelFees[i]));
            detail.setDailyAllowance(Integer.parseInt(dailyAllowances[i]));
            detail.setDays(Integer.parseInt(days[i]));
            detail.setExpenseTotal(Integer.parseInt(expenseTotals[i]));
            detail.setMemo(memos[i]);
        }

        // 4. 消える
        while (detailsInSession.size() > numSubmittedBlocks) {
            detailsInSession.remove(detailsInSession.size() - 1);
        }

        // 5. 新しいファイル処理
        Collection<Part> allParts = request.getParts();
        for (int i = 0; i < numSubmittedBlocks; i++) {
            String fileInputName = "receipt_allowance_" + i;
            List<Part> newFileParts = allParts.stream()
                .filter(part -> fileInputName.equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());

            if (!newFileParts.isEmpty()) {
                Step2Detail detail = detailsInSession.get(i);
                for (Part filePart : newFileParts) {
                    try {
                        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
                        String absoluteUploadPath = getServletContext().getRealPath(TEMP_UPLOAD_DIR);
                        File uploadDir = new File(absoluteUploadPath);
                        if (!uploadDir.exists()) uploadDir.mkdirs();
                        File savedFile = new File(uploadDir, uniqueFileName);
                        try (InputStream input = filePart.getInputStream()) {
                            Files.copy(input, savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                        UploadedFile uploadedFile = new UploadedFile();
                        uploadedFile.setOriginalFileName(originalFileName);
                        uploadedFile.setUniqueStoredName(uniqueFileName);
                        uploadedFile.setTemporaryPath(TEMP_UPLOAD_DIR + "/" + uniqueFileName);
                        detail.getTemporaryFiles().add(uploadedFile);
                    } catch (Exception e) {
                        System.err.println("Step 2のミス, index " + i + ": " + e.getMessage());
                    }
                }
            }
        }
        
        session.setAttribute("trip", trip);
        response.sendRedirect(request.getContextPath() + "/businessTripStep3");
    }
}