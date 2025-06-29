package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Step3Detail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transProject;
    private String departure;
    private String arrival;
    private String transport;
    private int fareAmount;
    private String transTripType;
    private String transBurden;
    private int transExpenseTotal;
    private String transMemo;
    private List<UploadedFile> temporaryFiles = new ArrayList<>();

    // --- Getters and Setters ---
    public String getTransProject() { return transProject; }
    public void setTransProject(String transProject) { this.transProject = transProject; }
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
    public String getTransBurden() { return transBurden; }
    public void setTransBurden(String transBurden) { this.transBurden = transBurden; }
    public int getTransExpenseTotal() { return transExpenseTotal; }
    public void setTransExpenseTotal(int transExpenseTotal) { this.transExpenseTotal = transExpenseTotal; }
    public String getTransMemo() { return transMemo; }
    public void setTransMemo(String transMemo) { this.transMemo = transMemo; }
    public List<UploadedFile> getTemporaryFiles() { return temporaryFiles; }
    public void setTemporaryFiles(List<UploadedFile> temporaryFiles) { this.temporaryFiles = temporaryFiles; }
}