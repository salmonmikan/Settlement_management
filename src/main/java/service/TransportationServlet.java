package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
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

import bean.Project;
import bean.TransportationApplicationBean;
import bean.TransportationDetailBean;
import bean.UploadedFile;
import dao.ProjectListDAO;

@WebServlet("/transportationRequest")
@MultipartConfig
public class TransportationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMP_UPLOAD_DIR = "/uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        TransportationApplicationBean transportationApp = (TransportationApplicationBean) session.getAttribute("transportationApp");

        if (transportationApp == null) {
            // Nếu người dùng vào thẳng URL này mà không qua Init, tạo mới để tránh lỗi
            transportationApp = new TransportationApplicationBean();
            session.setAttribute("transportationApp", transportationApp);
        }
        
        try {
            // Bước 1: Gọi DAO như bình thường.
            // Biến daoProjectList sẽ có kiểu List<ProjectListDAO.Project>
            ProjectListDAO projectDAO = new ProjectListDAO();
            List<ProjectListDAO.Project> daoProjectList = projectDAO.getAllProjects();

            // Bước 2: Tạo một danh sách mới, trống, với kiểu dữ liệu mà JSP cần (List<bean.Project>)
            // Đảm bảo bạn đã import bean.Project ở đầu file.
            List<Project> projectListForJsp = new ArrayList<>();

            // Bước 3: Dùng vòng lặp để chuyển đổi từng đối tượng.
            for (ProjectListDAO.Project daoProject : daoProjectList) {
                // Tạo một đối tượng bean.Project mới
                Project jspProject = new Project(daoProject.getId(), daoProject.getName());
                
                // Thêm đối tượng đã chuyển đổi vào danh sách mới
                projectListForJsp.add(jspProject);
            }

            // Bước 4: Đặt danh sách ĐÃ CHUYỂN ĐỔI vào request.
            // JSP sẽ nhận được đúng kiểu dữ liệu nó cần.
            request.setAttribute("projectList", projectListForJsp);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi, đặt một danh sách trống để tránh lỗi trên JSP
            request.setAttribute("projectList", new ArrayList<Project>());
        }

        request.setAttribute("transportationApp", transportationApp);
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/transportation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("transportationApp") == null) {
            response.sendRedirect(request.getContextPath() + "/transportationInit");
            return;
        }

        TransportationApplicationBean transportationApp = (TransportationApplicationBean) session.getAttribute("transportationApp");

        String filesToDeleteParam = request.getParameter("filesToDelete");
        if (filesToDeleteParam != null && !filesToDeleteParam.isEmpty()) {
            List<String> filesToDeleteList = Arrays.asList(filesToDeleteParam.split(","));
            String realPath = getServletContext().getRealPath("");
            for (TransportationDetailBean detail : transportationApp.getDetails()) {
                detail.getTemporaryFiles().removeIf(file -> {
                    boolean toDelete = filesToDeleteList.contains(file.getUniqueStoredName());
                    if (toDelete) {
                        try { Files.deleteIfExists(Paths.get(realPath + file.getTemporaryPath())); } catch (IOException e) { e.printStackTrace(); }
                    }
                    return toDelete;
                });
            }
        }
        
        List<TransportationDetailBean> oldDetails = new ArrayList<>(transportationApp.getDetails());
        transportationApp.getDetails().clear();

        String[] projectCodes = request.getParameterValues("projectCode[]");
        
        if (projectCodes != null && projectCodes.length > 0) {

            
            String[] dates = request.getParameterValues("date[]");
            String[] destinations = request.getParameterValues("destination[]");
            String[] departures = request.getParameterValues("departure[]");
            String[] arrivals = request.getParameterValues("arrival[]");
            String[] transports = request.getParameterValues("transport[]");
            String[] fareAmounts = request.getParameterValues("fareAmount[]");
            String[] transTripTypes = request.getParameterValues("transTripType[]");
            String[] burdens = request.getParameterValues("burden[]");
            String[] expenseTotals = request.getParameterValues("expenseTotal[]");
            String[] transMemos = request.getParameterValues("transMemo[]");
            String[] reports = request.getParameterValues("report[]");
            Collection<Part> allParts = request.getParts();

            for (int i = 0; i < projectCodes.length; i++) {
                TransportationDetailBean newDetail = new TransportationDetailBean();
                
                newDetail.setProjectCode(projectCodes[i]);
                newDetail.setDestination(destinations[i]);
                newDetail.setDate(dates[i]);
                newDetail.setDeparture(departures[i]);
                newDetail.setArrival(arrivals[i]);
                newDetail.setTransport(transports[i]);
                newDetail.setTransTripType(transTripTypes[i]);
                newDetail.setBurden(burdens[i]);
                newDetail.setTransMemo(transMemos[i]);
                newDetail.setReport(reports[i]);

                try {
                    newDetail.setFareAmount(fareAmounts[i] != null && !fareAmounts[i].isEmpty() ? Integer.parseInt(fareAmounts[i]) : 0);
                    newDetail.setExpenseTotal(expenseTotals[i] != null && !expenseTotals[i].isEmpty() ? Integer.parseInt(expenseTotals[i]) : 0);
                } catch (NumberFormatException e) {
                    newDetail.setFareAmount(0); newDetail.setExpenseTotal(0);
                }
                
                String fileInputName = "receipt_transportation_" + i;
                List<Part> newFileParts = allParts.stream()
                    .filter(part -> fileInputName.equals(part.getName()) && part.getSize() > 0)
                    .collect(Collectors.toList());

                if (i < oldDetails.size()) {
                    newDetail.setTemporaryFiles(oldDetails.get(i).getTemporaryFiles());
                }

                for (Part filePart : newFileParts) {
                    try {
                        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                        if(originalFileName == null || originalFileName.isBlank()) continue;

                        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
                        String tempPath = getServletContext().getRealPath(TEMP_UPLOAD_DIR);
                        File uploadDir = new File(tempPath);
                        if (!uploadDir.exists()) uploadDir.mkdirs();
                        Files.copy(filePart.getInputStream(), new File(uploadDir, uniqueFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        
                        UploadedFile uf = new UploadedFile();
                        uf.setOriginalFileName(originalFileName);
                        uf.setUniqueStoredName(uniqueFileName);
                        uf.setTemporaryPath(TEMP_UPLOAD_DIR + "/" + uniqueFileName);
                        newDetail.getTemporaryFiles().add(uf);
                    } catch (Exception e) { e.printStackTrace(); }
                }

                transportationApp.getDetails().add(newDetail);
            }
        }
        
        session.setAttribute("transportationApp", transportationApp);
        response.sendRedirect(request.getContextPath() + "/transportationConfirm");
    }
}