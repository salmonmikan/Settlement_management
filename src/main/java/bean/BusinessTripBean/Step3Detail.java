package bean.BusinessTripBean;

public class Step3Detail {
    private String transProject;
    private String departure;
    private String arrival;
    private String transport;
    private String fareAmount;
    private String transTripType;
    private String transBurden;
    private String transExpenseTotal;
    private String transMemo;

    public Step3Detail(String transProject, String departure, String arrival,
                       String transport, String fareAmount, String transTripType,
                       String transBurden, String transExpenseTotal, String transMemo) {
        this.transProject = transProject;
        this.departure = departure;
        this.arrival = arrival;
        this.transport = transport;
        this.fareAmount = fareAmount;
        this.transTripType = transTripType;
        this.transBurden = transBurden;
        this.transExpenseTotal = transExpenseTotal;
        this.transMemo = transMemo;
    }

    public String getTransProject() {
        return transProject;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getTransport() {
        return transport;
    }

    public String getFareAmount() {
        return fareAmount;
    }

    public String getTransTripType() {
        return transTripType;
    }

    public String getTransBurden() {
        return transBurden;
    }

    public String getTransExpenseTotal() {
        return transExpenseTotal;
    }

    public String getTransMemo() {
        return transMemo;
    }
}