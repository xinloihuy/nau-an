package com.mycompany.webhuongdannauan.utils;

public class YouTubeUtil {
    
    /**
     * Chuyển đổi URL xem YouTube sang URL nhúng (embed URL).
     * Ví dụ: https://www.youtube.com/watch?v=ABCDEFG -> https://www.youtube.com/embed/ABCDEFG
     */
    public static String getEmbedUrl(String watchUrl) {
        if (watchUrl == null || watchUrl.isEmpty()) {
            return null;
        }
        
        // Kiểm tra xem có phải là link xem (watch?v=) không
        if (watchUrl.contains("watch?v=")) {
            String videoId = watchUrl.substring(watchUrl.indexOf("v=") + 2);
            // Loại bỏ các tham số phụ (như &t=...) nếu có
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
            return "https://www.youtube.com/embed/" + videoId;
        } 
        
        // Nếu đã là link embed hoặc định dạng khác, trả về nguyên bản
        return watchUrl;
    }
}