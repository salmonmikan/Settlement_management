package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvLoader {
    private static final Properties env = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(".env")) {
            env.load(fis);
        } catch (IOException e) {
            System.err.println(".envファイルの読み込みに失敗しました: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return env.getProperty(key, System.getenv(key)); // 環境変数も fallback
    }
}
