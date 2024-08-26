package org.example;

import io.javalin.Javalin;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ServerTest {

    // OpenAI API key
    private static final String API_KEY = "39baa8d480a24d73a267a78310d54433";  // API密钥
    private static final String GPT_API_URL = "https://api.aimlapi.com/chat/completions";

    public static void main(String[] args) {

        // 创建Javalin服务器
        Javalin app = Javalin.create().start(7000);

        // 设置一个GET请求，处理GPT API的调用
        app.get("/gpt", ctx -> {
            String prompt = ctx.queryParam("prompt");
            if (prompt == null || prompt.isEmpty()) {
                ctx.status(400).result("Missing 'prompt' parameter");
                return;
            }

            try {
                String gptResponse = callGPTAPI(prompt);
                ctx.result(gptResponse);
            } catch (IOException e) {
                ctx.status(500).result("Error calling GPT API: " + e.getMessage());
            }
        });
    }

    // 调用GPT API的封装方法
    private static String callGPTAPI(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // 构造GPT请求体
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-4");
        jsonBody.put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));
        jsonBody.put("temperature", 0.7);

        // 创建HTTP请求
        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(GPT_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        // 发送请求并获取响应
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        // 解析响应并提取GPT的回答
        String responseBody = response.body().string();
        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        return choices.getJSONObject(0).getJSONObject("message").getString("content").trim();
    }
}

