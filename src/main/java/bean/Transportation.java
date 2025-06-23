package bean;

public class Transportation {

    private int transportationId;
    private String requestId;
    private java.time.LocalDate transportationDate;
    private String departureLocation;
    private String arrivalLocation;
    private String category;
    private String transportTypeId;
    private String settlementId;
    private String abstractNote;
    private String payer;
    private String receipt;
    private int amount;

    public int getTransportationId() { return transportationId; }
    public void setTransportationId(int transportationId) { this.transportationId = transportationId; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public java.time.LocalDate getTransportationDate() { return transportationDate; }
    public void setTransportationDate(java.time.LocalDate transportationDate) { this.transportationDate = transportationDate; }

    public String getDepartureLocation() { return departureLocation; }
    public void setDepartureLocation(String departureLocation) { this.departureLocation = departureLocation; }

    public String getArrivalLocation() { return arrivalLocation; }
    public void setArrivalLocation(String arrivalLocation) { this.arrivalLocation = arrivalLocation; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTransportTypeId() { return transportTypeId; }
    public void setTransportTypeId(String transportTypeId) { this.transportTypeId = transportTypeId; }

    public String getSettlementId() { return settlementId; }
    public void setSettlementId(String settlementId) { this.settlementId = settlementId; }

    public String getAbstractNote() { return abstractNote; }
    public void setAbstractNote(String abstractNote) { this.abstractNote = abstractNote; }

    public String getPayer() { return payer; }
    public void setPayer(String payer) { this.payer = payer; }

    public String getReceipt() { return receipt; }
    public void setReceipt(String receipt) { this.receipt = receipt; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
