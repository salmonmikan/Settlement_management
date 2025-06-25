package bean.BusinessTripBean;

public class Step1Data {

 private String projectCode;
 private String report;
 private String startDate;
 private String endDate;
 private String totalDays;
 
 public Step1Data(String startDate, String endDate, String projectCode, String report, String totalDays) {
     this.startDate = startDate;
     this.endDate = endDate;
     this.projectCode = projectCode;
     this.report = report;
     this.totalDays = totalDays;
 }
 
 public String getStartDate() { return startDate; }
 public String getEndDate() { return endDate; }
 public String getProjectCode() { return projectCode; }
 public String getReport() { return report; }
 public String getTotalDays() { return totalDays; }
 

}
