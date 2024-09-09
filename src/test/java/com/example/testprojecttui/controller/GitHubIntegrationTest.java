package com.example.testprojecttui.controller;

import com.example.testprojecttui.dto.GitHubRepositoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testIntegrationGetRepositories() {
        webTestClient.get()
                .uri("/github/NikitaDmitrenkoS/repositories")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GitHubRepositoryResponse.class)
                .hasSize(2);
    }
}
