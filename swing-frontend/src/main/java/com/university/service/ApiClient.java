package com.university.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.university.model.ApiResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient client;
    private final Gson gson;

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
        this.gson = new GsonBuilder().create();
    }

    public <T> CompletableFuture<ApiResponse<T>> get(String path, TypeToken<ApiResponse<T>> typeToken) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> gson.fromJson(response.body(), typeToken.getType()));
    }

    public <T> CompletableFuture<ApiResponse<T>> post(String path, Object body, TypeToken<ApiResponse<T>> typeToken) {
        String json = gson.toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> gson.fromJson(response.body(), typeToken.getType()));
    }

    public <T> CompletableFuture<ApiResponse<T>> put(String path, Object body, TypeToken<ApiResponse<T>> typeToken) {
        String json = gson.toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> gson.fromJson(response.body(), typeToken.getType()));
    }

    public CompletableFuture<Void> delete(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> null);
    }
}
