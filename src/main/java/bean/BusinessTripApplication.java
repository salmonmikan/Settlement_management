package bean;

public class BusinessTripApplication {

    private String requestId;
    private String projectCode;
    private String employeeId;
    private String categoryId;
    private String report;

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }
}
