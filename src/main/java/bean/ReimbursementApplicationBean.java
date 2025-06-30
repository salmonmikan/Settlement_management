package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 立替金申請全体の状態を保持するセッション用Bean。
 * <p>
 * 各申請明細のリストと、合計金額を保持します。
 */
public class ReimbursementApplicationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 明細のリスト */
    private List<ReimbursementDetailBean> details = new ArrayList<>();

    /** 合計金額 */
    private int totalAmount;

    /**
     * 明細一覧から合計金額を再計算します。
     * <p>
     * 各明細の金額を合算して、totalAmountにセットします。
     */

    private int applicationId;
//    private int setApplicationId;
    public void calculateTotalAmount() {
        this.totalAmount = (this.details == null) ? 0 :
            this.details.stream().mapToInt(ReimbursementDetailBean::getAmount).sum();
    }

    /**
     * 明細リストを取得します。
     * 
     * @return 立替明細のリスト
     */
    public List<ReimbursementDetailBean> getDetails() {
        return details;
    }

    /**
     * 明細リストを設定します。
     * 
     * @param details 明細のリスト
     */
    public void setDetails(List<ReimbursementDetailBean> details) {
        this.details = details;
    }

    /**
     * 合計金額を取得します。
     * 
     * @return 合計金額
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     * 合計金額を設定します。
     * 
     * @param totalAmount 合計金額
     */
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    
	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
}
