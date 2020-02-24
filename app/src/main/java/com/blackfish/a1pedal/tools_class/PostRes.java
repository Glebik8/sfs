package com.blackfish.a1pedal.tools_class;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostRes {
    public final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String post(String url, String json, String token) throws IOException {
        RequestBody body = (RequestBody) RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }}