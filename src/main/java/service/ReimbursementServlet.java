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
import bean.ReimbursementApplicationBean;
import bean.ReimbursementDetailBean;
import bean.UploadedFile;
import dao.ProjectDAO;
import dao.ProjectListDAO;

@WebServlet("/reimbursement")
@MultipartConfig
public class ReimbursementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMP_UPLOAD_DIR = "/uploads"; // Thư mục tạm

 // Gợi ý: Thay thế toàn bộ method doGet trong ReimbursementServlet.java bằng phiên bản này.

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ReimbursementApplicationBean reimbursement = (ReimbursementApplicationBean) session.getAttribute("reimbursement");

        // Nếu không có đơn nào trong session (lần đầu truy cập), tạo một đơn mới và lưu vào session.
        if (reimbursement == null) {
            reimbursement = new ReimbursementApplicationBean();
            session.setAttribute("reimbursement", reimbursement);
        }
        
        // XÓA BỎ logic tự động thêm detail rỗng ở đây.
        // Việc này sẽ do JavaScript trên JSP đảm nhiệm một cách thông minh hơn.

        // Lấy danh sách project để hiển thị trong dropdown
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

        // Đặt bean vào request để JSP có thể truy cập và hiển thị
        request.setAttribute("reimbursement", reimbursement);
        request.getRequestDispatcher("/WEB-INF/views/serviceJSP/reimbursement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("reimbursement") == null) {
            response.sendRedirect(request.getContextPath() + "/reimbursementInit");
            return;
        }

        ReimbursementApplicationBean reimbursement = (ReimbursementApplicationBean) session.getAttribute("reimbursement");

        // Xử lý các file được đánh dấu xóa
        String filesToDeleteParam = request.getParameter("filesToDelete");
        if (filesToDeleteParam != null && !filesToDeleteParam.isEmpty()) {
            List<String> filesToDeleteList = Arrays.asList(filesToDeleteParam.split(","));
            String realPath = getServletContext().getRealPath("");
            for (ReimbursementDetailBean detail : reimbursement.getDetails()) {
                 detail.getTemporaryFiles().removeIf(file -> {
                    boolean toDelete = filesToDeleteList.contains(file.getUniqueStoredName());
                    if (toDelete) {
                        try { Files.deleteIfExists(Paths.get(realPath + file.getTemporaryPath())); } catch (IOException e) { e.printStackTrace(); }
                    }
                    return toDelete;
                });
            }
        }
        
        List<ReimbursementDetailBean> oldDetails = new ArrayList<>(reimbursement.getDetails());
        reimbursement.getDetails().clear();

        // Dùng projectCode[] làm cơ sở lặp vì nó đáng tin cậy hơn
        String[] projectCodes = request.getParameterValues("projectCode[]");
        
        if (projectCodes != null && projectCodes.length > 0) {
            Collection<Part> allParts = request.getParts();
            String[] dates = request.getParameterValues("date[]");
            String[] destinations = request.getParameterValues("destinations[]");
            String[] reports = request.getParameterValues("report[]");
            String[] accountingItems = request.getParameterValues("accountingItem[]");
            String[] amounts = request.getParameterValues("amount[]");

            for (int i = 0; i < projectCodes.length; i++) {
                ReimbursementDetailBean newDetail = new ReimbursementDetailBean();
                
                newDetail.setProjectCode(projectCodes[i]);
                newDetail.setDate(dates[i]);
                newDetail.setDestinations(destinations[i]);
                newDetail.setReport(reports[i]);
                newDetail.setAccountingItem(accountingItems[i]);
                try {
                    newDetail.setAmount(amounts[i] != null && !amounts[i].isEmpty() ? Integer.parseInt(amounts[i]) : 0);
                } catch (NumberFormatException e) { newDetail.setAmount(0); }

                String fileInputName = "receipt_reimbursement_" + i;
                List<Part> newFileParts = allParts.stream()
                    .filter(part -> fileInputName.equals(part.getName()) && part.getSize() > 0)
                    .collect(Collectors.toList());

                // Logic xử lý file sao chép 1-1 từ BusinessTrip
                if (!newFileParts.isEmpty()) {
                    // Nếu có file mới, ta sẽ giữ lại các file cũ chưa bị xóa của block này
                    if (i < oldDetails.size()) {
                        newDetail.setTemporaryFiles(oldDetails.get(i).getTemporaryFiles());
                    }
                    // Và thêm file mới vào
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
                            uf.setBlockIndex(i);
                            uf.setTemporaryPath(TEMP_UPLOAD_DIR + "/" + uniqueFileName);
                            newDetail.getTemporaryFiles().add(uf);
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                } else {
                    // Không có file mới, bảo toàn toàn bộ file cũ
                    if (i < oldDetails.size()) {
                        newDetail.setTemporaryFiles(oldDetails.get(i).getTemporaryFiles());
                    }
                }
                reimbursement.getDetails().add(newDetail);
            }
        }
        
        session.setAttribute("reimbursement", reimbursement);
        response.sendRedirect(request.getContextPath() + "/reimbursementConfirm");
    }
}