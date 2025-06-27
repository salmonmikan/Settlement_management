package bean.BusinessTripBean;

import java.io.Serializable;

public class Step1Data implements Serializable {
    private String startDate;
    private String endDate;
    private String projectCode;
    private String report;
    private int totalDays;

    // Getters & Setters
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }
}