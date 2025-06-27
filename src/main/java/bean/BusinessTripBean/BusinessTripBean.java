package bean.BusinessTripBean;

import java.io.Serializable;
import java.util.List;

import bean.BusinessTripForm;

public class BusinessTripBean implements Serializable {
    private Step1Data step1Data;
    private List<Step2Detail> step2List;
    private List<Step3Detail> step3List;

    public BusinessTripBean() {
    }

    public BusinessTripBean(BusinessTripForm form) {
        Step1Data s1 = new Step1Data();
        s1.setStartDate(form.getStartDate());
        s1.setEndDate(form.getEndDate());
        s1.setProjectCode(form.getProjectCode());
        s1.setReport(form.getTripReport());
        s1.setTotalDays(form.getTotalDays());
        this.step1Data = s1;

        this.step2List = form.getAllowanceList().stream().map(d -> {
            Step2Detail s2 = new Step2Detail();
            s2.setRegionType(d.regionType);
            s2.setTripType(d.tripType);
            s2.setHotel(d.hotel);
            s2.setBurden(d.burden);
            s2.setHotelFee(d.hotelFee);
            s2.setDailyAllowance(d.dailyAllowance);
            s2.setDays(d.days);
            s2.setExpenseTotal(d.expenseTotal);
            s2.setMemo(d.memo);
            s2.setReceiptFileNames(d.receiptFileNames);
            return s2;
        }).toList();

        this.step3List = form.getTransportList().stream().map(d -> {
            Step3Detail s3 = new Step3Detail();
            s3.setTransProject(d.transProject);
            s3.setDeparture(d.departure);
            s3.setArrival(d.arrival);
            s3.setTransport(d.transport);
            s3.setFareAmount(d.fareAmount);
            s3.setTransTripType(d.transTripType);
            s3.setTransBurden(d.burden);
            s3.setTransExpenseTotal(d.expenseTotal);
            s3.setTransMemo(d.memo);
            s3.setReceiptFileNames(d.receiptFileNames);
            return s3;
        }).toList();
    }

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

    public int getTotalStep2Amount() {
        return step2List == null ? 0 :
               step2List.stream().mapToInt(Step2Detail::getExpenseTotal).sum();
    }

    public int getTotalStep3Amount() {
        return step3List == null ? 0 :
               step3List.stream().mapToInt(Step3Detail::getTransExpenseTotal).sum();
    }
}