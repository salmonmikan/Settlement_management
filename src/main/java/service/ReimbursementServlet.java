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
import dao.ProjectListDAO;
@WebServlet("/reimbursement")
@MultipartConfig
public class ReimbursementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEMP_UPLOAD_DIR = "/temp_uploads";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ReimbursementApplicationBean reimbursement = (ReimbursementApplicationBean) session.getAttribute("reimbursement");
        if (reimbursement == null) {
            reimbursement = new ReimbursementApplicationBean();
            session.setAttribute("reimbursement", reimbursement);
        }
        try {
            ProjectListDAO projectDAO = new ProjectListDAO();
            List<ProjectListDAO.Project> daoProjectList = projectDAO.getAllProjects();
            List<Project> projectListForJsp = new ArrayList<>();
            for (ProjectListDAO.Project daoProject : daoProjectList) {
                Project jspProject = new Project(daoProject.getId(), daoProject.getName());
                projectListForJsp.add(jspProject);
            }
            request.setAttribute("projectList", projectListForJsp);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("projectList", new ArrayList<Project>());
        }
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
        if (filesToDeleteParam != null && !filesToDeleteParam.trim().isEmpty()) {
            List<String> filesToDeleteList = Arrays.asList(filesToDeleteParam.split(","));
            String realPath = getServletContext().getRealPath("");
            for (ReimbursementDetailBean detail : reimbursement.getDetails()) {
                 detail.getTemporaryFiles().removeIf(file -> {
                    boolean toDelete = filesToDeleteList.contains(file.getUniqueStoredName());
                    if (toDelete && file.getTemporaryPath() != null) {
                        try { Files.deleteIfExists(Paths.get(realPath + file.getTemporaryPath())); } catch (IOException e) { e.printStackTrace(); }
                    }
                    return toDelete;
                });
            }
        }
        // Logic "Cập nhật tại chỗ"
        String[] projectCodes = request.getParameterValues("projectCode[]");
        List<ReimbursementDetailBean> detailsInSession = reimbursement.getDetails();
        int numSubmittedBlocks = (projectCodes != null) ? projectCodes.length : 0;
        List<ReimbursementDetailBean> updatedDetails = new ArrayList<>();
        String[] dates = request.getParameterValues("date[]");
        String[] destinations = request.getParameterValues("destinations[]");
        String[] abstractNotes = request.getParameterValues("abstractNote[]");
        String[] reports = request.getParameterValues("report[]");
        String[] accountingItems = request.getParameterValues("accountingItem[]");
        String[] amounts = request.getParameterValues("amount[]");
        for (int i = 0; i < numSubmittedBlocks; i++) {
            ReimbursementDetailBean detail;
            if (i < detailsInSession.size()) {
                detail = detailsInSession.get(i);
            } else {
                detail = new ReimbursementDetailBean();
            }
            detail.setProjectCode(projectCodes[i]);
            detail.setDate(dates[i]);
            detail.setDestinations(destinations[i]);
            detail.setReport(reports[i]);
            detail.setAccountingItem(accountingItems[i]);
            detail.setAbstractNote(abstractNotes[i]);
            try {
                detail.setAmount(amounts[i] != null && !amounts[i].isEmpty() ? Integer.parseInt(amounts[i]) : 0);
            } catch (NumberFormatException e) { detail.setAmount(0); }
            updatedDetails.add(detail);
        }
        reimbursement.setDetails(updatedDetails);
        Collection<Part> allParts = request.getParts();
        for (int i = 0; i < numSubmittedBlocks; i++) {
            String fileInputName = "receipt_reimbursement_" + i;
            List<Part> newFileParts = allParts.stream()
                .filter(part -> fileInputName.equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());
            if (!newFileParts.isEmpty()) {
                ReimbursementDetailBean detail = reimbursement.getDetails().get(i);
                // Nếu là luồng tạo mới hoặc có file mới, ta sẽ xóa file cũ (nếu có) trong bean và thêm file mới
//                detail.getTemporaryFiles().clear();
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
                        detail.getTemporaryFiles().add(uf);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
        session.setAttribute("reimbursement", reimbursement);
        response.sendRedirect(request.getContextPath() + "/reimbursementConfirm");
    }
}