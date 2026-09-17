package com.example.pagination.controller;

import com.example.pagination.dto.ExternalApiResponse;
import com.example.pagination.service.ExternalApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integration")
public class ExternalApiController {
    private final ExternalApiService externalApiService;

    public ExternalApiController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping("/google-status")
    public ExternalApiResponse googleStatus() {
        return externalApiService.checkGoogle();
    }
}
