package com.example.testprojecttui.controller;


import com.example.testprojecttui.dto.GitHubRepositoryResponse;
import com.example.testprojecttui.exceptions.UserNotFoundException;
import com.example.testprojecttui.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@WebFluxTest(GitHubController.class)
public class GitHubControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GitHubService gitHubService;

    @Test
    public void testGetRepositoriesSuccess() {
        // Mock the service call
        Mockito.when(gitHubService.getUserRepositories(anyString()))
                .thenReturn(Mono.just(List.of(new GitHubRepositoryResponse("repo1", "owner1", List.of()))));

        webTestClient.get()
                .uri("/github/NikitaDmitrenkoS/repositories")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GitHubRepositoryResponse.class)
                .hasSize(1)
                .value(response -> assertEquals("repo1", response.get(0).getRepositoryName()));
    }

    @Test
    public void testUserNotFound() {
        Mockito.when(gitHubService.getUserRepositories(anyString()))
                .thenReturn(Mono.error(new UserNotFoundException("User not found")));

        webTestClient.get()
                .uri("/github/NikitaDmitrenkoS1/repositories")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.Message").isEqualTo("User not found");
    }

    @Test
    public void testUnsupportedMediaType() {
        webTestClient.get()
                .uri("/github/NikitaDmitrenkoS1/repositories")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody()
                .jsonPath("$.status").isEqualTo(406)
                .jsonPath("$.Message").isEqualTo("Only JSON format is supported");
    }
}

