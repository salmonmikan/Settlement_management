package bean.BusinessTripBean;

public class TransportationRequest {
	private String transportationRequestId;
    private String staffId;
    private String projectCode;
    private String date;
    private String departureStation;
    private String arrivalStation;
    private int amount;
    private String category;
    private String transportType;
    private String payer;
    private int totalAmount;
    private String abstractNote;

    // Getter và Setter
    public TransportationRequest() {}
    
    public TransportationRequest(String transportationRequestId, String staffId, String projectCode, String date,
            String departureStation, String arrivalStation, String category, String transportType,
            String payer, int amount, int totalAmount, String abstractNote) {
				this.transportationRequestId = transportationRequestId;
				this.staffId = staffId;
				this.projectCode = projectCode;
				this.date = date;
				this.departureStation = departureStation;
				this.arrivalStation = arrivalStation;
				this.category = category;
				this.transportType = transportType;
				this.payer = payer;
				this.amount = amount;
				this.totalAmount = totalAmount;
				this.abstractNote = abstractNote;
				}
    public String getTransportationRequestId() { return transportationRequestId; }
    public void setTransportationRequestId(String transportationRequestId) { this.transportationRequestId = transportationRequestId; }

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }

    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDepartureStation() { return departureStation; }
    public void setDepartureStation(String departureStation) { this.departureStation = departureStation; }

    public String getArrivalStation() { return arrivalStation; }
    public void setArrivalStation(String arrivalStation) { this.arrivalStation = arrivalStation; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }

    public String getPayer() { return payer; }
    public void setPayer(String payer) { this.payer = payer; }
    
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public String getAbstractNote() { return abstractNote; }
    public void setAbstractNote(String abstractNote) { this.abstractNote = abstractNote; }
}
