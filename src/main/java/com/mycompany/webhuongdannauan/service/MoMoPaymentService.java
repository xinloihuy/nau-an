package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.utils.MoMoSecurity;
import com.mycompany.webhuongdannauan.config.MoMoConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;

public class MoMoPaymentService {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public String createPaymentRequest(String orderId, long amount, String orderInfo, String extraData, String userId) {
        try {
            // Đảm bảo extraData là chuỗi rỗng nếu null
            String finalExtraData = (extraData != null && !extraData.isEmpty()) ? extraData : "";
            String requestId = orderId;

            // Chuỗi raw để ký
            String rawSignature = "accessKey=" + MoMoConfig.ACCESS_KEY +
                    "&amount=" + amount +
                    "&extraData=" + finalExtraData +
                    "&ipnUrl=" + MoMoConfig.NOTIFY_URL +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + MoMoConfig.PARTNER_CODE +
                    "&redirectUrl=" + MoMoConfig.RETURN_URL +
                    "&requestId=" + requestId +
                    "&requestType=" + MoMoConfig.REQUEST_TYPE;

            String signature = MoMoSecurity.hmacSHA256(rawSignature, MoMoConfig.SECRET_KEY);

            // Tạo JSON body
            JsonObject json = new JsonObject();
            json.addProperty("partnerCode", MoMoConfig.PARTNER_CODE);
            json.addProperty("accessKey", MoMoConfig.ACCESS_KEY);
            json.addProperty("requestId", requestId);
            json.addProperty("amount", amount);
            json.addProperty("orderId", orderId);
            json.addProperty("orderInfo", orderInfo);
            json.addProperty("redirectUrl", MoMoConfig.RETURN_URL);
            json.addProperty("ipnUrl", MoMoConfig.NOTIFY_URL);
            json.addProperty("extraData", finalExtraData);
            json.addProperty("requestType", MoMoConfig.REQUEST_TYPE);
            json.addProperty("signature", signature);

            // Gửi request đến api endpoint của momo
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
//            RequestBody body = RequestBody.create(json.toString(), JSON);
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(MoMoConfig.ENDPOINT)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    // Nếu lỗi 400, in ra body response để biết MoMo báo lỗi gì
                    String errorBody = response.body() != null ? response.body().string() : "No body";
                    throw new IOException("Unexpected response: " + response.code() + " - " + errorBody);
                }

                String responseBody = response.body().string();
                JsonObject respJson = gson.fromJson(responseBody, JsonObject.class);

                // Dù MoMo trả về 200, nhưng resultCode phải bằng 0 mới thành công
                if (respJson.has("resultCode") && respJson.get("resultCode").getAsInt() == 0) {
                    return respJson.get("payUrl").getAsString();
                } else {
                    throw new RuntimeException("MoMo API rejected request (resultCode != 0): " + responseBody);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Ném RuntimeException để Servlet bắt và chuyển hướng tới trang lỗi
            throw new RuntimeException("Failed to create MoMo payment request.", e);
        }
    }

    // Giữ nguyên phương thức verifyPaymentCallback vì nó đã đúng
    public boolean verifyPaymentCallback(String signature, JsonObject data) {
        try {
            // Tái tạo chuỗi Raw Signature theo ĐÚNG CÁC TRƯỜNG VÀ THỨ TỰ CỦA MỠ MỖ CALLBACK
            // Bắt buộc phải ký tất cả các tham số MoMo gửi về trong URL.

            // Cần đảm bảo các trường này tồn tại trong JsonObject data (từ MoMoReturnServlet)

            String rawSignature =
                    "accessKey=" + MoMoConfig.ACCESS_KEY + // DÙ không gửi về, MoMo yêu cầu ký accessKey
                            "&amount=" + data.get("amount").getAsString() +
                            "&extraData=" + (data.has("extraData") && !data.get("extraData").isJsonNull() ? data.get("extraData").getAsString() : "") +
                            "&message=" + data.get("message").getAsString() +
                            "&orderId=" + data.get("orderId").getAsString() +
                            "&orderInfo=" + data.get("orderInfo").getAsString() +
                            "&orderType=" + data.get("orderType").getAsString() +
                            "&partnerCode=" + data.get("partnerCode").getAsString() +
                            "&payType=" + data.get("payType").getAsString() +
                            "&requestId=" + data.get("requestId").getAsString() +
                            "&responseTime=" + data.get("responseTime").getAsString() +
                            "&resultCode=" + data.get("resultCode").getAsString() +
                            "&transId=" + data.get("transId").getAsString(); // Lấy từ request.getParameter("transId")

            // In ra để kiểm tra chuỗi Raw Signature này
            System.out.println("Callback Raw Signature: " + rawSignature);

            return MoMoSecurity.verifySignature(signature, rawSignature, MoMoConfig.SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}