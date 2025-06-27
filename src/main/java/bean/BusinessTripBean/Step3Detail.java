package bean.BusinessTripBean;

import java.io.Serializable;
import java.util.List;

public class Step3Detail implements Serializable {

    private String transProject;
    private String departure;
    private String arrival;
    private String transport;
    private int fareAmount;
    private String transTripType;
    private String burden;
    private String memo;
    private int transExpenseTotal;
    private List<String> receiptFileNames;

    // Getter / Setter chuẩn cho field
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

    public String getBurden() { return burden; }
    public void setBurden(String burden) { this.burden = burden; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public int getTransExpenseTotal() { return transExpenseTotal; }
    public void setTransExpenseTotal(int transExpenseTotal) { this.transExpenseTotal = transExpenseTotal; }

    public List<String> getReceiptFileNames() { return receiptFileNames; }
    public void setReceiptFileNames(List<String> receiptFileNames) {
        this.receiptFileNames = receiptFileNames;
    }
    public String getTransBurden() {
        return this.burden;
    }

    public String getTransMemo() {
        return this.memo;
    }

    // 🔁 Alias method để giữ nguyên tên bạn mong muốn

    public void setTransBurden(String burden) {
        this.burden = burden;
    }

    public void setTransMemo(String memo) {
        this.memo = memo;
    }
}