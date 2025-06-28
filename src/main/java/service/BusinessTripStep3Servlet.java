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
import bean.Step3Detail;
import bean.UploadedFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/businessTripStep3")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 15)
public class BusinessTripStep3Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Ở phiên bản này, chúng ta vẫn dùng đường dẫn vật lý tuyệt đối
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
        request.getRequestDispatcher("/WEB-INF/views/businessTrip/businessTrip3.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("trip") == null) {
            response.sendRedirect(request.getContextPath() + "/businessTripInit");
            return;
        }

        BusinessTripBean trip = (BusinessTripBean) session.getAttribute("trip");
        trip.getStep3Details().clear();

        // Sửa đổi điểm 2: Lấy đường dẫn vật lý đúng cách
        String absoluteTempPath = getServletContext().getRealPath(TEMP_UPLOAD_DIR);
        File uploadDir = new File(absoluteTempPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String[] transProjects = request.getParameterValues("transProject[]");
        
        if (transProjects != null) {
            String[] departures = request.getParameterValues("departure[]");
            String[] arrivals = request.getParameterValues("arrival[]");
            String[] transports = request.getParameterValues("transport[]");
            String[] fareAmounts = request.getParameterValues("fareAmount[]");
            String[] transTripTypes = request.getParameterValues("transTripType[]");
            String[] transBurdens = request.getParameterValues("transBurden[]");
            String[] expenseTotals = request.getParameterValues("expenseTotal[]");
            String[] transMemos = request.getParameterValues("transMemo[]");

            for (int i = 0; i < transProjects.length; i++) {
                Step3Detail detail = new Step3Detail();
                try {
                    detail.setTransProject(transProjects[i]);
                    detail.setDeparture(departures[i]);
                    detail.setArrival(arrivals[i]);
                    detail.setTransport(transports[i]);
                    detail.setFareAmount(Integer.parseInt(fareAmounts[i]));
                    detail.setTransTripType(transTripTypes[i]);
                    detail.setTransBurden(transBurdens[i]);
                    detail.setTransExpenseTotal(Integer.parseInt(expenseTotals[i]));
                    detail.setTransMemo(transMemos[i]);
                } catch(Exception e) {
                     System.err.println("Lỗi parse dữ liệu ở Step 3, index " + i + ": " + e.getMessage());
                }

                // Xử lý upload file
                try {
                    String fileInputName = "receipt_transport_" + i;
                    for(Part filePart : request.getParts()) {
                        if (filePart.getName().equals(fileInputName) && filePart.getSize() > 0) {
                            String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
                            
                            File fileToSave = new File(uploadDir, uniqueFileName);

                            try (InputStream input = filePart.getInputStream()) {
                                Files.copy(input, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }

                            UploadedFile uploadedFile = new UploadedFile();
                            uploadedFile.setOriginalFileName(originalFileName);
                            uploadedFile.setUniqueStoredName(uniqueFileName);
                            
                            // Sửa đổi điểm 3: Lưu đúng đường dẫn web
                            uploadedFile.setTemporaryPath(TEMP_UPLOAD_DIR + "/" + uniqueFileName);
                            
                            detail.getTemporaryFiles().add(uploadedFile);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                trip.getStep3Details().add(detail);
            }
        }
        
        session.setAttribute("trip", trip);

        String action = request.getParameter("action");
        if ("go_back".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/businessTripStep2");
        } else {
            response.sendRedirect(request.getContextPath() + "/businessTripConfirm");
        }
    }
}