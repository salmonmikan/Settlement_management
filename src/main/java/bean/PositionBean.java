package bean;

import java.io.Serializable;

/**
 * 役職情報を保持するBeanクラス。
 * <p>
 * 役職ID、役職名、論理削除フラグなどの情報を管理します。
 */
public class PositionBean implements Serializable {
    private String position_id;
    private String position_name;
    private int delete_flag;

    /**
     * デフォルトコンストラクタ。
     */
    public PositionBean() {
    }

    /**
     * IDと名前を指定して役職情報を初期化するコンストラクタ。
     * 
     * @param position_id   役職ID
     * @param position_name 役職名
     */
    public PositionBean(String position_id, String position_name) {
        this.position_id = position_id;
        this.position_name = position_name;
    }

    /**
     * 役職IDを取得します。
     * 
     * @return 役職ID
     */
    public String getPosition_id() {
        return position_id;
    }

    /**
     * 役職IDを設定します。
     * 
     * @param position_id 役職ID
     */
    public void setPosition_id(String position_id) {
        this.position_id = position_id;
    }

    /**
     * 役職名を取得します。
     * 
     * @return 役職名
     */
    public String getPosition_name() {
        return position_name;
    }

    /**
     * 役職名を設定します。
     * 
     * @param position_name 役職名
     */
    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    /**
     * 論理削除フラグを取得します。
     * 
     * @return 削除フラグ（0=通常、1=削除済、9=削除不可など）
     */
    public int getDelete_flag() {
        return delete_flag;
    }

    /**
     * 論理削除フラグを設定します。
     * 
     * @param delete_flag 削除フラグ
     */
    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }
}
