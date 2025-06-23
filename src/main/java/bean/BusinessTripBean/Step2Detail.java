package bean.BusinessTripBean;



public class Step2Detail {
    private String regionType;
    private String tripType;
    private String hotel;
    private String burden;
    private String hotelFee;
    private String dailyAllowance;
    private String days;
    private String expenseTotal;
    private String memo;
    
    //reimbursement用
    private String accountingItem;
    //private String receipt;
    private int amount;

    public Step2Detail(String regionType, String tripType, String hotel, String burden,
                       String hotelFee, String dailyAllowance, String days,
                       String expenseTotal, String memo) {
        this.regionType = regionType;
        this.tripType = tripType;
        this.hotel = hotel;
        this.burden = burden;
        this.hotelFee = hotelFee;
        this.dailyAllowance = dailyAllowance;
        this.days = days;
        this.expenseTotal = expenseTotal;
        this.memo = memo;
    }

  //reimbursement用
    public Step2Detail(String accountingItem,String memo,int amount) {
    	this.accountingItem = accountingItem;
    	this.memo = memo;
    	this.amount = amount;
    }

    public String getRegionType() { return regionType; }
    public String getTripType() { return tripType; }
    public String getHotel() { return hotel; }
    public String getBurden() { return burden; }
    public String getHotelFee() { return hotelFee; }
    public String getDailyAllowance() { return dailyAllowance; }
    public String getDays() { return days; }
    public String getExpenseTotal() { return expenseTotal; }
    public String getMemo() { return memo; }
    
    //reimbursement用
    public String getAccountingItem() { return accountingItem; }
    public int getAmount() { return amount; }
}