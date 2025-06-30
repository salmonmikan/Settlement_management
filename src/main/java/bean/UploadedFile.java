// Trong file bean/UploadedFile.java
package bean;

import java.io.Serializable;

/**
 * アップロードされたファイルの情報を保持するBeanクラス。
 * 元ファイル名、保存ファイル名、一時パス、保存パス、所属ブロックのインデックスなどを含む。
 */
public class UploadedFile implements Serializable {
    private static final long serialVersionUID = 1L;

    // アップロードされたファイルの元のファイル名
    private String originalFileName;

    // 保存時に一意になるよう変換されたファイル名
    private String uniqueStoredName;

    // 一時的にアップロードされたファイルの保存先
    private String temporaryPath;

    // 永続的に保存されるファイルの保存先パス
    private String storedFilePath;

    // ★★★ THÊM THUỘC TÍNH NÀY VÀO ★★★
    // UIや入力ブロック単位の識別に使われるインデックス番号
    private int blockIndex;

    // --- Getters and Setters ---

    /**
     * 元のファイル名を取得します。
     */
    public String getOriginalFileName() { return originalFileName; }

    /**
     * 元のファイル名を設定します。
     */
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    /**
     * 保存用に一意に設定されたファイル名を取得します。
     */
    public String getUniqueStoredName() { return uniqueStoredName; }

    /**
     * 保存用に一意に設定されたファイル名を設定します。
     */
    public void setUniqueStoredName(String uniqueStoredName) { this.uniqueStoredName = uniqueStoredName; }

    /**
     * 一時保存のパスを取得します。
     */
    public String getTemporaryPath() { return temporaryPath; }

    /**
     * 一時保存のパスを設定します。
     */
    public void setTemporaryPath(String temporaryPath) { this.temporaryPath = temporaryPath; }

    /**
     * 保存先のファイルパスを取得します。
     */
    public String getStoredFilePath() { return storedFilePath; }

    /**
     * 保存先のファイルパスを設定します。
     */
    public void setStoredFilePath(String storedFilePath) { this.storedFilePath = storedFilePath; }

    // ★★★ THÊM GETTER/SETTER NÀY VÀO ★★★

    /**
     * ブロックインデックスを取得します（どの入力ブロックに紐づくかを示す）。
     */
    public int getBlockIndex() { return blockIndex; }

    /**
     * ブロックインデックスを設定します。
     */
    public void setBlockIndex(int blockIndex) { this.blockIndex = blockIndex; }
}
