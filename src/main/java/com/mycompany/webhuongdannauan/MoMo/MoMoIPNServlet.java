package com.mycompany.webhuongdannauan.MoMo;

import com.mycompany.webhuongdannauan.service.MoMoPaymentService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

@WebServlet("/momo-ipn")
public class MoMoIPNServlet extends HttpServlet {

    private final MoMoPaymentService momoService = new MoMoPaymentService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String jsonData = sb.toString();
        JsonObject data = gson.fromJson(jsonData, JsonObject.class);

        String signature = data.get("signature").getAsString();
        boolean isValid = momoService.verifyPaymentCallback(signature, data);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (!isValid) {
            JsonObject err = new JsonObject();
            err.addProperty("status", "error");
            err.addProperty("message", "Invalid signature");
            out.print(gson.toJson(err));
            return;
        }

        String resultCode = data.get("resultCode").getAsString();
        if ("0".equals(resultCode)) {
            String orderId = data.get("orderId").getAsString();
            String extraData = data.get("extraData").getAsString();

            try {
                String decoded = new String(Base64.getDecoder().decode(extraData));
                JsonObject extra = gson.fromJson(decoded, JsonObject.class);
                String userId = extra.get("userId").getAsString();
                String packageType = extra.get("packageType").getAsString();

                System.out.println("✅ Premium activated for user: " + userId + ", package: " + packageType);
                // TODO: Cập nhật DB

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JsonObject ok = new JsonObject();
        ok.addProperty("status", "success");
        ok.addProperty("message", "OK");
        out.print(gson.toJson(ok));
    }
}