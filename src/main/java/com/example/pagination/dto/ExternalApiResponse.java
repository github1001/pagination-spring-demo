package com.example.pagination.dto;

public record ExternalApiResponse(String url, int statusCode, boolean success) {
}
