package com.mycompany.webhuongdannauan.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import java.util.logging.Logger;

public class EnvConfig {
    private static final Logger logger = Logger.getLogger(EnvConfig.class.getName());
    private static Dotenv dotenv = null;

    static {
    try {
        String projectDir = System.getProperty("user.dir");
        System.out.println("📂 Dotenv search dir: " + projectDir);
        System.out.println("📂 Current working directory: " + System.getProperty("user.dir"));


        dotenv = Dotenv.configure()
                .directory("D:/") // 🔥 ép đọc file .env tại project gốc
                .ignoreIfMissing()
                .load();

        System.out.println("✅ .env loaded successfully.");
        System.out.println("🔹 DB_URL from .env: " + dotenv.get("DB_URL"));
    } catch (DotenvException e) {
        System.out.println("⚠️ Không tìm thấy file .env, sử dụng system environment variables");
        dotenv = null;
    }
}



    public static String get(String key) {
        String sysVal = System.getenv(key);
        if (sysVal != null) return sysVal;
        if (dotenv != null) return dotenv.get(key);
        return null;
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }
}
