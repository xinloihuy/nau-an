package com.mycompany.webhuongdannauan.config;

public class MoMoConfig {
    // Thông tin từ MoMo Developer (Test environment)
    public static final String PARTNER_CODE = "MOMOBKUN20180529";
    public static final String ACCESS_KEY = "klm05TvNBzhg7h7j";
    public static final String SECRET_KEY = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa";
    public static final String ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create"; // ✅ không dấu cách

    // DÙNG NGROK KHI TEST LOCAL
    public static final String RETURN_URL = "http://localhost:8080/momo-return";   // ← Thay YOUR_NGROK_URL
    public static final String NOTIFY_URL = "http://localhost:8080/momo-ipn";      // ← Thay YOUR_NGROK_URL

    public static final String REQUEST_TYPE = "captureWallet";
}