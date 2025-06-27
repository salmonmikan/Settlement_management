package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bean.BusinessTripBean.BusinessTripBean;
import bean.BusinessTripBean.Step1Data;
import bean.BusinessTripBean.Step2Detail;
import bean.BusinessTripBean.Step3Detail;

public class BusinessTripForm implements Serializable {

    // === Step 1 ===
    private String startDate;
    private String endDate;
    private String projectCode;
    private String tripReport;
    private int totalDays;

    private BusinessTripBean tripBean;
    private BusinessTripBean businessTripBean;

    public void buildBusinessTripBean() {
        BusinessTripBean bean = new BusinessTripBean();

        // Step1
        Step1Data s1 = new Step1Data();
        s1.setStartDate(startDate);
        s1.setEndDate(endDate);
        s1.setProjectCode(projectCode);
        s1.setReport(tripReport);
        s1.setTotalDays(totalDays);
        bean.setStep1Data(s1);

        // Step2
        List<Step2Detail> step2List = new ArrayList<>();
        for (AllowanceDetail d : allowanceList) {
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
            System.out.println("🧾 Step2: Attached receipts = " + d.receiptFileNames);
            s2.setReceiptFileNames(d.receiptFileNames); // <- PHẢI copy!
            step2List.add(s2);
        }
        bean.setStep2List(step2List);

        // Step3
        List<Step3Detail> step3List = new ArrayList<>();
        for (TransportDetail d : transportList) {
            Step3Detail s3 = new Step3Detail();
            s3.setTransProject(d.transProject);
            s3.setDeparture(d.departure);
            s3.setArrival(d.arrival);
            s3.setTransport(d.transport);
            s3.setFareAmount(d.fareAmount);
            s3.setTransTripType(d.transTripType);
            s3.setTransBurden(d.burden); // 🧠 Dùng alias setter
            s3.setTransMemo(d.memo);     // 🧠 Dùng alias setter
            s3.setTransExpenseTotal(d.expenseTotal);
            System.out.println("🧾 Step3: Attached receipts = " + d.receiptFileNames);
            s3.setReceiptFileNames(d.receiptFileNames); // <- PHẢI copy!
            step3List.add(s3);
        }
        bean.setStep3List(step3List);

        this.businessTripBean = bean;
        System.out.println("✅ buildBusinessTripBean done. step2.size = " + step2List.size() + ", step3.size = " + step3List.size());
    }

    public BusinessTripBean getBusinessTripBean() {
        return this.businessTripBean;
    }

    public void setBusinessTripBean(BusinessTripBean tripBean) {
        this.tripBean = tripBean;
    }

    // === Step 2: Allowance Detail ===
    public static class AllowanceDetail {
        public String regionType;
        public String tripType;
        public String hotel;
        public String burden;
        public int hotelFee;
        public int dailyAllowance;
        public int days;
        public int expenseTotal;
        public String memo;
        public List<String> receiptFileNames = new ArrayList<>();
    }

    private List<AllowanceDetail> allowanceList = new ArrayList<>();

    // === Step 3: Transportation Detail ===
    public static class TransportDetail {
        public String transProject;
        public String departure;
        public String arrival;
        public String transport;
        public int fareAmount;
        public String transTripType;
        public String burden;
        public int expenseTotal;
        public String memo;
        public List<String> receiptFileNames = new ArrayList<>();
    }

    private List<TransportDetail> transportList = new ArrayList<>();

    // ==== Getter/Setter ====
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    public String getTripReport() { return tripReport; }
    public void setTripReport(String tripReport) { this.tripReport = tripReport; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public List<AllowanceDetail> getAllowanceList() { return allowanceList; }
    public void setAllowanceList(List<AllowanceDetail> allowanceList) { this.allowanceList = allowanceList; }

    public List<TransportDetail> getTransportList() { return transportList; }
    public void setTransportList(List<TransportDetail> transportList) { this.transportList = transportList; }
}