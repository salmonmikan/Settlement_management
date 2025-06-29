package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransportationApplicationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<TransportationDetailBean> details = new ArrayList<>();
    private int totalAmount;

    public void calculateTotalAmount() {
        this.totalAmount = (this.details == null) ? 0 : 
            this.details.stream().mapToInt(TransportationDetailBean::getExpenseTotal).sum();
    }

    
    public List<TransportationDetailBean> getDetails() { return details; }
    public void setDetails(List<TransportationDetailBean> details) { this.details = details; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
}