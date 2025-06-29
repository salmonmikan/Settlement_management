package bean;

import java.io.Serializable;

public class Step1Data implements Serializable {
    private static final long serialVersionUID = 1L;

    private String startDate;
    private String endDate;
    private int totalDays;
    private String projectCode;
    private String tripReport;

    // --- Getters and Setters ---
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public String getTripReport() { return tripReport; }
    public void setTripReport(String tripReport) { this.tripReport = tripReport; }
}