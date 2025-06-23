package bean.BusinessTripBean;

public class Step1Data {
//共通
 private String projectCode;
 private String report;
 
//BusinessTrip用
 private String startDate;
 private String endDate;
 private String totalDays;
 
 //reimbursement用
 private String date; 
 
 public Step1Data(String startDate, String endDate, String projectCode, String report, String totalDays) {
     this.startDate = startDate;
     this.endDate = endDate;
     this.projectCode = projectCode;
     this.report = report;
     this.totalDays = totalDays;
 }
 
//reimbursement用
 public Step1Data(String date,String projectCode,String report) {
	 this.date = date;
	 this.projectCode = projectCode;
	 this.report = report;
 }
// public void setDate(String date) {
//	 this.date = date;
//}
// public void setProjectCode(String projectCode) {
//	 this.projectCode = projectCode;
// }
// public void setReport(String report) {
//	 this.report = report;
// }
 
 public String getStartDate() { return startDate; }
 public String getEndDate() { return endDate; }
 public String getProjectCode() { return projectCode; }
 public String getReport() { return report; }
 public String getTotalDays() { return totalDays; }
 
 //reimbursement用
 public String getDate() { return date; }
}