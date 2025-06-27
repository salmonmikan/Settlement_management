package bean.BusinessTripBean;

import java.io.Serializable;
import java.util.List;

public class Step2Detail implements Serializable {
    private String regionType;
    private String tripType;
    private String hotel;
    private String burden;
    private int hotelFee;
    private int dailyAllowance;
    private int days;
    private int expenseTotal;
    private String memo;
    private List<String> receiptFileNames;

    // Getters & Setters
    public String getRegionType() { return regionType; }
    public void setRegionType(String regionType) { this.regionType = regionType; }

    public String getTripType() { return tripType; }
    public void setTripType(String tripType) { this.tripType = tripType; }

    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }

    public String getBurden() { return burden; }
    public void setBurden(String burden) { this.burden = burden; }

    public int getHotelFee() { return hotelFee; }
    public void setHotelFee(int hotelFee) { this.hotelFee = hotelFee; }

    public int getDailyAllowance() { return dailyAllowance; }
    public void setDailyAllowance(int dailyAllowance) { this.dailyAllowance = dailyAllowance; }

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }

    public int getExpenseTotal() { return expenseTotal; }
    public void setExpenseTotal(int expenseTotal) { this.expenseTotal = expenseTotal; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public List<String> getReceiptFileNames() { return receiptFileNames; }
    public void setReceiptFileNames(List<String> receiptFileNames) {
        this.receiptFileNames = receiptFileNames;
    }
}