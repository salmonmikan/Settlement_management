package bean;

/**
 * プロジェクト情報を簡易的に表すBeanクラス。
 * <p>
 * 主にIDと名称のみを保持します。
 */
public class Project {
    private String id;
    private String name;

    /**
     * プロジェクトのIDと名称を指定してインスタンスを生成します。
     * 
     * @param id   プロジェクトID
     * @param name プロジェクト名
     */
    public Project(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * プロジェクトIDを取得します。
     * 
     * @return プロジェクトID
     */
    public String getId() {
        return id;
    }

    /**
     * プロジェクト名を取得します。
     * 
     * @return プロジェクト名
     */
    public String getName() {
        return name;
    }
}
