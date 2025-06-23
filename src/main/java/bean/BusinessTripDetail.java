package bean;

import java.time.LocalDate;

public class BusinessTripDetail {

    private int lodgingId;
    private String requestId;
    private String categoryId;
    private java.time.LocalDate tripStartDate;
    private java.time.LocalDate tripEndDate;
    private int numberOfDays;
    private int nightAllowance;
    private String payer;
    private String receipt;

    public int getLodgingId() { return lodgingId; }
    public void setLodgingId(int lodgingId) { this.lodgingId = lodgingId; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public LocalDate getTripStartDate() { return tripStartDate; }
    public void setTripStartDate(java.time.LocalDate tripStartDate) { this.tripStartDate = tripStartDate; }

    public LocalDate getTripEndDate() { return tripEndDate; }
    public void setTripEndDate(java.time.LocalDate tripEndDate) { this.tripEndDate = tripEndDate; }

    public int getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(int numberOfDays) { this.numberOfDays = numberOfDays; }

    public int getNightAllowance() { return nightAllowance; }
    public void setNightAllowance(int nightAllowance) { this.nightAllowance = nightAllowance; }

    public String getPayer() { return payer; }
    public void setPayer(String payer) { this.payer = payer; }

    public String getReceipt() { return receipt; }
    public void setReceipt(String receipt) { this.receipt = receipt; }
}
