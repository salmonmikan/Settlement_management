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
import bean.Step3Detail;
import bean.UploadedFile;

@WebServlet("/businessTripStep3")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 15)
public class BusinessTripStep3Servlet extends HttpServlet {
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

        if (trip.getStep3Details().isEmpty()) {
            trip.getStep3Details().add(new Step3Detail());
        }
        
        request.setAttribute("trip", trip);
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/businessTrip3.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        String action = request.getParameter("action");
        if ("go_back".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/businessTripStep2");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");

        String filesToDeleteParam = request.getParameter("filesToDelete");
        if (filesToDeleteParam != null && !filesToDeleteParam.isEmpty()) {
            List<String> filesToDeleteList = List.of(filesToDeleteParam.split(","));
//            String realPath = getServletContext().getRealPath(""); 
         // 1. Ghi nhận các file cần xóa vào bean session
            trip.getFilesToDelete().addAll(filesToDeleteList);
         // 2. Chỉ xóa tham chiếu file khỏi danh sách chi tiết, KHÔNG xóa file vật lý
            for (Step3Detail detail : trip.getStep3Details()) {
                detail.getTemporaryFiles().removeIf(file -> filesToDeleteList.contains(file.getUniqueStoredName()));
            }
        }

        String[] transProjects = request.getParameterValues("transProject[]");
        List<Step3Detail> detailsInSession = trip.getStep3Details();
        int numSubmittedBlocks = (transProjects != null) ? transProjects.length : 0;
        
        String[] departures = request.getParameterValues("departure[]");
        String[] arrivals = request.getParameterValues("arrival[]");
        String[] transports = request.getParameterValues("transport[]");
        String[] fareAmounts = request.getParameterValues("fareAmount[]");
        String[] transTripTypes = request.getParameterValues("transTripType[]");
        String[] transBurdens = request.getParameterValues("transBurden[]");
        String[] expenseTotals = request.getParameterValues("expenseTotal[]");
        String[] transMemos = request.getParameterValues("transMemo[]");

        for (int i = 0; i < numSubmittedBlocks; i++) {
            Step3Detail detail;
            if (i < detailsInSession.size()) {
                detail = detailsInSession.get(i);
            } else {
                detail = new Step3Detail();
                detailsInSession.add(detail);
            }

            detail.setTransProject(transProjects[i]);
            detail.setDeparture(departures[i]);
            detail.setArrival(arrivals[i]);
            detail.setTransport(transports[i]);
            detail.setFareAmount(Integer.parseInt(fareAmounts[i]));
            detail.setTransTripType(transTripTypes[i]);
            detail.setTransBurden(transBurdens[i]);
            detail.setTransExpenseTotal(Integer.parseInt(expenseTotals[i]));
            detail.setTransMemo(transMemos[i]);
        }

        while (detailsInSession.size() > numSubmittedBlocks) {
            detailsInSession.remove(detailsInSession.size() - 1);
        }

        Collection<Part> allParts = request.getParts();
        for (int i = 0; i < numSubmittedBlocks; i++) {
            String fileInputName = "receipt_transport_" + i;
            List<Part> newFileParts = allParts.stream()
                .filter(part -> fileInputName.equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());

            if (!newFileParts.isEmpty()) {
                Step3Detail detail = detailsInSession.get(i);
                
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
                        System.err.println("Step 3のミス, index " + i + ": " + e.getMessage());
                    }
                }
            }
        }
        
        session.setAttribute("trip", trip);
        response.sendRedirect(request.getContextPath() + "/businessTripConfirm");
    }
}