package bean;

/**
 * 交通費申請のリクエスト情報を表すBeanクラス。
 * 各申請は、プロジェクト、出発地、到着地、交通手段、金額、負担区分などの情報を含みます。
 */
public class TransportationRequest {
    private String transportationRequestId;
    private String projectCode;
    private String date;
    private String destination;
    private String departureStation;
    private String arrivalStation;
    private int amount;
    private String category;
    private String transportType;
    private String payer;
    private int totalAmount;
    private String abstractNote;
    private String report;

    /**
     * デフォルトコンストラクタ。
     */
    public TransportationRequest() {}

    /**
     * 全項目を指定するコンストラクタ。
     * @param transportationRequestId 申請ID
     * @param staffId 社員ID
     * @param projectCode プロジェクトコード
     * @param date 日付
     * @param departureStation 出発地
     * @param arrivalStation 到着地
     * @param category 区分（用途等）
     * @param transportType 交通手段
     * @param payer 費用負担者
     * @param amount 金額
     * @param totalAmount 合計金額
     * @param abstractNote 摘要
     */
    public TransportationRequest(String transportationRequestId, String projectCode, String date,String destination,
                                 String departureStation, String arrivalStation, String category, String transportType,
                                 String payer, int amount, int totalAmount, String abstractNote, String report) {
        this.transportationRequestId = transportationRequestId;
        this.projectCode = projectCode;
        this.date = date;
        this.destination = destination; 
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.category = category;
        this.transportType = transportType;
        this.payer = payer;
        this.amount = amount;
        this.totalAmount = totalAmount;
        this.abstractNote = abstractNote;
        this.report = report;
    }

    /**
     * 申請IDを取得します。
     */
    public String getTransportationRequestId() { return transportationRequestId; }

    /**
     * 申請IDを設定します。
     */
    public void setTransportationRequestId(String transportationRequestId) { this.transportationRequestId = transportationRequestId; }

    /**
     * プロジェクトコードを取得します。
     */
    public String getProjectCode() { return projectCode; }

    /**
     * プロジェクトコードを設定します。
     */
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    /**
     * 日付を取得します。
     */
    public String getDate() { return date; }

    /**
     * 日付を設定します。
     */
    public void setDate(String date) { this.date = date; }
    
    
    //ソンが追加する
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    /**
     * 出発地を取得します。
     */
    public String getDepartureStation() { return departureStation; }

    /**
     * 出発地を設定します。
     */
    public void setDepartureStation(String departureStation) { this.departureStation = departureStation; }

    /**
     * 到着地を取得します。
     */
    public String getArrivalStation() { return arrivalStation; }

    /**
     * 到着地を設定します。
     */
    public void setArrivalStation(String arrivalStation) { this.arrivalStation = arrivalStation; }

    /**
     * 区分（例：出張・私用など）を取得します。
     */
    public String getCategory() { return category; }

    /**
     * 区分を設定します。
     */
    public void setCategory(String category) { this.category = category; }

    /**
     * 交通手段を取得します。
     */
    public String getTransportType() { return transportType; }

    /**
     * 交通手段を設定します。
     */
    public void setTransportType(String transportType) { this.transportType = transportType; }

    /**
     * 費用負担者を取得します。
     */
    public String getPayer() { return payer; }

    /**
     * 費用負担者を設定します。
     */
    public void setPayer(String payer) { this.payer = payer; }

    /**
     * 金額を取得します。
     */
    public int getAmount() { return amount; }

    /**
     * 金額を設定します。
     */
    public void setAmount(int amount) { this.amount = amount; }

    /**
     * 合計金額を取得します。
     */
    public int getTotalAmount() { return totalAmount; }

    /**
     * 合計金額を設定します。
     */
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    /**
     * 摘要（備考）を取得します。
     */
    public String getAbstractNote() { return abstractNote; }

    /**
     * 摘要（備考）を設定します。
     */
    public void setAbstractNote(String abstractNote) { this.abstractNote = abstractNote; }
    
    
  //ソンが追加する
    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }
}
