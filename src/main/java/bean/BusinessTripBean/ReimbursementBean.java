package bean.BusinessTripBean;

import java.util.List;

public class ReimbursementBean {
	private Step1Data step1;
	private List<Step2Detail> step2List;

    public ReimbursementBean() {}

    public ReimbursementBean(Step1Data step1, List<Step2Detail> step2List) {
        this.step1 = step1;
        this.step2List = step2List;
    }

    // Getter & Setter
    public Step1Data getStep1() {
        return step1;
    }

    public void setStep1(Step1Data step1) {
        this.step1 = step1;
    }

    public List<Step2Detail> getStep2List() {
        return step2List;
    }

    public void setStep2List(List<Step2Detail> step2List) {
        this.step2List = step2List;
    }
}
