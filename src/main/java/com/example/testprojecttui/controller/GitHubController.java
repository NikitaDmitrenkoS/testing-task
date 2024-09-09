package com.example.testprojecttui.controller;

import com.example.testprojecttui.exceptions.UserNotFoundException;
import com.example.testprojecttui.service.GitHubService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/github")
@AllArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;


    @GetMapping(value = "/{username}/repositories", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> getRepositories(
            @RequestHeader(value = "Accept", required = false) String acceptHeader, @PathVariable String username) {

        if (acceptHeader == null || !acceptHeader.equals(APPLICATION_JSON_VALUE)) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Map.of("status", 406, "Message", "Only JSON format is supported")));
        }

        return gitHubService.getUserRepositories(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")))
                .map(repos -> ResponseEntity.ok((Object) repos)) // Cast the successful result to Object
                .onErrorResume(UserNotFoundException.class, ex -> Mono.just(
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.<String, Object>of("status", HttpStatus.NOT_FOUND.value(), "Message", ex.getMessage()))));

    }
}
