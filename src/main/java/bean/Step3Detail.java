package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 出張申請のStep3（交通費明細）の情報を保持するBeanクラス。
 * プロジェクトコード、出発地、到着地、交通手段、交通費、メモ、添付ファイルなどを管理します。
 */
public class Step3Detail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String transProject;
    private String departure;
    private String arrival;
    private String transport;
    private int fareAmount;
    private String transTripType;
    private String transBurden;
    private int transExpenseTotal;
    private String transMemo;
    private List<UploadedFile> temporaryFiles = new ArrayList<>();

    // --- Getters and Setters ---

    /**
     * プロジェクトコードを取得します。
     * @return プロジェクトコード
     */
    public String getTransProject() { return transProject; }

    /**
     * プロジェクトコードを設定します。
     * @param transProject プロジェクトコード
     */
    public void setTransProject(String transProject) { this.transProject = transProject; }

    /**
     * 出発地を取得します。
     * @return 出発地
     */
    public String getDeparture() { return departure; }

    /**
     * 出発地を設定します。
     * @param departure 出発地
     */
    public void setDeparture(String departure) { this.departure = departure; }

    /**
     * 到着地を取得します。
     * @return 到着地
     */
    public String getArrival() { return arrival; }

    /**
     * 到着地を設定します。
     * @param arrival 到着地
     */
    public void setArrival(String arrival) { this.arrival = arrival; }

    /**
     * 交通手段を取得します。
     * @return 交通手段
     */
    public String getTransport() { return transport; }

    /**
     * 交通手段を設定します。
     * @param transport 交通手段
     */
    public void setTransport(String transport) { this.transport = transport; }

    /**
     * 交通費金額を取得します。
     * @return 交通費（円）
     */
    public int getFareAmount() { return fareAmount; }

    /**
     * 交通費金額を設定します。
     * @param fareAmount 交通費（円）
     */
    public void setFareAmount(int fareAmount) { this.fareAmount = fareAmount; }

    /**
     * 出張区分を取得します。
     * @return 出張区分
     */
    public String getTransTripType() { return transTripType; }

    /**
     * 出張区分を設定します。
     * @param transTripType 出張区分
     */
    public void setTransTripType(String transTripType) { this.transTripType = transTripType; }

    /**
     * 費用負担区分を取得します。
     * @return 負担区分
     */
    public String getTransBurden() { return transBurden; }

    /**
     * 費用負担区分を設定します。
     * @param transBurden 負担区分
     */
    public void setTransBurden(String transBurden) { this.transBurden = transBurden; }

    /**
     * 合計金額を取得します。
     * @return 合計金額（円）
     */
    public int getTransExpenseTotal() { return transExpenseTotal; }

    /**
     * 合計金額を設定します。
     * @param transExpenseTotal 合計金額（円）
     */
    public void setTransExpenseTotal(int transExpenseTotal) { this.transExpenseTotal = transExpenseTotal; }

    /**
     * メモを取得します。
     * @return メモ
     */
    public String getTransMemo() { return transMemo; }

    /**
     * メモを設定します。
     * @param transMemo メモ
     */
    public void setTransMemo(String transMemo) { this.transMemo = transMemo; }

    /**
     * 一時添付ファイルのリストを取得します。
     * @return 添付ファイルリスト
     */
    public List<UploadedFile> getTemporaryFiles() { return temporaryFiles; }

    /**
     * 一時添付ファイルのリストを設定します。
     * @param temporaryFiles 添付ファイルリスト
     */
    public void setTemporaryFiles(List<UploadedFile> temporaryFiles) { this.temporaryFiles = temporaryFiles; }
}
