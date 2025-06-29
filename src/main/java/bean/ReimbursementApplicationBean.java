package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementApplicationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ReimbursementDetailBean> details = new ArrayList<>();
    private int totalAmount;

    public void calculateTotalAmount() {
        this.totalAmount = (this.details == null) ? 0 : this.details.stream().mapToInt(ReimbursementDetailBean::getAmount).sum();
    }

    // Getters and Setters
    public List<ReimbursementDetailBean> getDetails() { return details; }
    public void setDetails(List<ReimbursementDetailBean> details) { this.details = details; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
}