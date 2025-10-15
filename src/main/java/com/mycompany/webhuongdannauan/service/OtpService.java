package com.mycompany.webhuongdannauan.service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OtpService {
    private static class OtpEntry {
        String otp;
        Instant expiresAt;

        OtpEntry(String otp, Instant expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }

    // Singleton
    private static final OtpService INSTANCE = new OtpService();

    public static OtpService getInstance() {
        return INSTANCE;
    }

    // Cache lưu email -> OtpEntry
    private final ConcurrentHashMap<String, OtpEntry> otpMap = new ConcurrentHashMap<>();

    // ScheduledExecutor để dọn dẹp OTP hết hạn định kỳ
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    private static final long OTP_LIFETIME_SECONDS = 300; // 5 phút

    private OtpService() {
        // Dọn dẹp mỗi phút
        cleaner.scheduleAtFixedRate(this::cleanExpired, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * Lưu OTP cho email (ghi đè nếu đã có)
     */
    public void put(String email, String otp) {
        otpMap.put(email, new OtpEntry(otp, Instant.now().plusSeconds(OTP_LIFETIME_SECONDS)));
    }

    /**
     * Lấy OTP nếu còn hạn (trả về null nếu hết hạn hoặc không tồn tại)
     */
    public String get(String email) {
        OtpEntry entry = otpMap.get(email);
        if (entry == null || entry.isExpired()) {
            otpMap.remove(email);
            return null;
        }
        return entry.otp;
    }

    /**
     * Xác minh OTP do người dùng nhập
     */
    public boolean verify(String email, String inputOtp) {
        String cached = get(email);
        if (cached != null && cached.equals(inputOtp)) {
            otpMap.remove(email); // xóa sau khi dùng
            return true;
        }
        return false;
    }

    /**
     * Xóa OTP hết hạn
     */
    private void cleanExpired() {
        Instant now = Instant.now();
        for (Map.Entry<String, OtpEntry> e : otpMap.entrySet()) {
            if (e.getValue().expiresAt.isBefore(now)) {
                otpMap.remove(e.getKey());
            }
        }
    }
}
