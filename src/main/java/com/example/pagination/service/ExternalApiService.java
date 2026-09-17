package com.example.pagination.service;

import com.example.pagination.dto.ExternalApiResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ExternalApiService {
    private static final String GOOGLE_URL = "https://www.google.com";
    private final HttpClient httpClient;

    public ExternalApiService() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build());
    }

    ExternalApiService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ExternalApiResponse checkGoogle() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(GOOGLE_URL))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
        try {
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return new ExternalApiResponse(GOOGLE_URL, response.statusCode(),
                    response.statusCode() >= 200 && response.statusCode() < 400);
        } catch (IOException e) {
            throw new IllegalStateException("External API call failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("External API call was interrupted", e);
        }
    }
}
