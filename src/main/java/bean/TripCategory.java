package bean;

public class TripCategory {

    private String categoryId;
    private String tripType;
    private int dailyAllowance;
    private int hotelFee;
    private String regionType;

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getTripType() { return tripType; }
    public void setTripType(String tripType) { this.tripType = tripType; }

    public int getDailyAllowance() { return dailyAllowance; }
    public void setDailyAllowance(int dailyAllowance) { this.dailyAllowance = dailyAllowance; }

    public int getHotelFee() { return hotelFee; }
    public void setHotelFee(int hotelFee) { this.hotelFee = hotelFee; }

    public String getRegionType() { return regionType; }
    public void setRegionType(String regionType) { this.regionType = regionType; }
}
