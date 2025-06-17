package bean.BusinessTripBean;

import java.util.ArrayList;
import java.util.List;

public class BusinessTripBean {
    private Step1Data step1Data;
    private List<Step2Detail> step2List;
    private List<Step3Detail> step3List;

    // ✅ Constructor mặc định (fix lỗi gạch đỏ khi new)
    public BusinessTripBean() {
        this.step2List = new ArrayList<>();
        this.step3List = new ArrayList<>();
    }

    // ✅ Constructor có tham số (nếu cần)
    public BusinessTripBean(Step1Data step1Data, List<Step2Detail> step2List, List<Step3Detail> step3List) {
        this.step1Data = step1Data;
        this.step2List = step2List;
        this.step3List = step3List;
    }

    // Getter & Setter
    public Step1Data getStep1Data() {
        return step1Data;
    }

    public void setStep1Data(Step1Data step1Data) {
        this.step1Data = step1Data;
    }

    public List<Step2Detail> getStep2List() {
        return step2List;
    }

    public void setStep2List(List<Step2Detail> step2List) {
        this.step2List = step2List;
    }

    public List<Step3Detail> getStep3List() {
        return step3List;
    }

    public void setStep3List(List<Step3Detail> step3List) {
        this.step3List = step3List;
    }
}