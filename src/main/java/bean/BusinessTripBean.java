package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 出張申請プロセス全体の状態をセッションに保持するためのBeanクラス。
 * <p>
 * 出張申請はステップ1〜3の入力プロセスで構成されており、
 * 各ステップのデータを一括で保持し、確認画面や登録処理で使用されます。
 */
public class BusinessTripBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Step1Data step1Data;
    private List<Step2Detail> step2Details;
    private List<Step3Detail> step3Details;
    private int totalAmount;
    private List<String> filesToDelete = new ArrayList<>();
    /**
     * デフォルトコンストラクタ。
     * ステップ1〜3のリストを初期化します。
     */
    public BusinessTripBean() {
        this.step1Data = new Step1Data();
        this.step2Details = new ArrayList<>();
        this.step3Details = new ArrayList<>();
    }
    
    /**
     * ステップ2とステップ3の費用を集計し、合計金額を算出します。
     * <p>
     * 確認画面へ遷移する前に呼び出され、画面表示や登録に使用される合計額を設定します。
     */
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

    /**
     * ステップ1のデータを取得します。
     * @return Step1Data オブジェクト
     */
    public Step1Data getStep1Data() {
        return step1Data;
    }

    /**
     * ステップ1のデータを設定します。
     * @param step1Data Step1Data オブジェクト
     */
    public void setStep1Data(Step1Data step1Data) {
        this.step1Data = step1Data;
    }

    /**
     * ステップ2の明細リストを取得します。
     * @return Step2Detail のリスト
     */
    public List<Step2Detail> getStep2Details() {
        return step2Details;
    }

    /**
     * ステップ2の明細リストを設定します。
     * @param step2Details Step2Detail のリスト
     */
    public void setStep2Details(List<Step2Detail> step2Details) {
        this.step2Details = step2Details;
    }

    /**
     * ステップ3の明細リストを取得します。
     * @return Step3Detail のリスト
     */
    public List<Step3Detail> getStep3Details() {
        return step3Details;
    }

    /**
     * ステップ3の明細リストを設定します。
     * @param step3Details Step3Detail のリスト
     */
    public void setStep3Details(List<Step3Detail> step3Details) {
        this.step3Details = step3Details;
    }

    /**
     * 合計金額を取得します。
     * @return 合計金額（円）
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     * 合計金額を設定します。
     * @param totalAmount 合計金額（円）
     */
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    public List<String> getFilesToDelete() {
        return filesToDelete;
    }

    public void setFilesToDelete(List<String> filesToDelete) {
        this.filesToDelete = filesToDelete;
    }
}
