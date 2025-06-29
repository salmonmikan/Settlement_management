package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 出張申請プロセス全体の状態を保持するセッションBean。
 * (Bean trong session để giữ trạng thái của toàn bộ quá trình đăng ký công tác)
 */
public class BusinessTripBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Step1Data step1Data;
    private List<Step2Detail> step2Details;
    private List<Step3Detail> step3Details;
    private int totalAmount;

    public BusinessTripBean() {
        this.step1Data = new Step1Data();
        this.step2Details = new ArrayList<>();
        this.step3Details = new ArrayList<>();
    }

    // Phương thức tính tổng tiền, được gọi từ Servlet trước khi hiển thị trang Confirm
    public void calculateTotalAmount() {
        int step2Total = 0;
        if (this.step2Details != null) {
            for (Step2Detail detail : this.step2Details) {
                step2Total += detail.getExpenseTotal();
            }
        }
        int step3Total = 0;
        if (this.step3Details != null) {
            for (Step3Detail detail : this.step3Details) {
                step3Total += detail.getTransExpenseTotal();
            }
        }
        this.totalAmount = step2Total + step3Total;
    }

    // --- Getters and Setters ---
    public Step1Data getStep1Data() { return step1Data; }
    public void setStep1Data(Step1Data step1Data) { this.step1Data = step1Data; }
    public List<Step2Detail> getStep2Details() { return step2Details; }
    public void setStep2Details(List<Step2Detail> step2Details) { this.step2Details = step2Details; }
    public List<Step3Detail> getStep3Details() { return step3Details; }
    public void setStep3Details(List<Step3Detail> step3Details) { this.step3Details = step3Details; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
}