// File: src/main/java/bean/BusinessTripBean/BusinessTripApplication.java
package bean;

import java.io.Serializable;
import java.time.LocalDate;

public class BusinessTripApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tripApplicationId;
    private int applicationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectCode;
    private String report;
    private int totalDays;
    private String staffId;

    // --- Getters and Setters ---
    public int getTripApplicationId() {
        return tripApplicationId;
    }

    public void setTripApplicationId(int tripApplicationId) {
        this.tripApplicationId = tripApplicationId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}