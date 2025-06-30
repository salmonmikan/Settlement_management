package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransportationDetailBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Các trường này tương ứng với các cột trong table 'transportation_request'
    // và các name attribute trong form transportation.jsp
    private int transportationId;
    private int applicationId;
    private String projectCode;
    private String date; // input type="date" trả về String YYYY-MM-DD
    private String destination;
    private String departure;
    private String arrival;
    private String transport;
    private int fareAmount;
    private String transTripType;
    private String burden;
    private int expenseTotal; // Trường được tính toán, không có trong DB
    private String transMemo;
    private String report;
   
    
    // Danh sách file đính kèm cho block này
    private List<UploadedFile> temporaryFiles = new ArrayList<>();

    // --- Getters và Setters ---

    public int getTransportationId() { return transportationId; }
    public void setTransportationId(int transportationId) { this.transportationId = transportationId; }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    public String getArrival() { return arrival; }
    public void setArrival(String arrival) { this.arrival = arrival; }
    
    public String getTransport() { return transport; }
    public void setTransport(String transport) { this.transport = transport; }
    
    public int getFareAmount() { return fareAmount; }
    public void setFareAmount(int fareAmount) { this.fareAmount = fareAmount; }

    public String getTransTripType() { return transTripType; }
    public void setTransTripType(String transTripType) { this.transTripType = transTripType; }

    public String getBurden() { return burden; }
    public void setBurden(String burden) { this.burden = burden; }
    
    public int getExpenseTotal() { return expenseTotal; }
    public void setExpenseTotal(int expenseTotal) { this.expenseTotal = expenseTotal; }

    public String getTransMemo() { return transMemo; }
    public void setTransMemo(String transMemo) { this.transMemo = transMemo; }

    public List<UploadedFile> getTemporaryFiles() { return temporaryFiles; }
    public void setTemporaryFiles(List<UploadedFile> temporaryFiles) { this.temporaryFiles = temporaryFiles; }
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	//ソンが追加する
    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }
}