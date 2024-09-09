package com.example.testprojecttui.service;

import com.example.testprojecttui.dto.Branch;
import com.example.testprojecttui.dto.BranchResponse;
import com.example.testprojecttui.dto.GitHubRepository;
import com.example.testprojecttui.dto.GitHubRepositoryResponse;
import com.example.testprojecttui.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class GitHubService {

    private final WebClient gitHubWebClient;


    public Mono<List<GitHubRepositoryResponse>> getUserRepositories(String username) {

        // Fetching repositories for the user
        return gitHubWebClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(el->!el.is2xxSuccessful(), clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new UserNotFoundException("User not found"));
                    }
                    return Mono.error(new RuntimeException("Client error"));
                })
                .bodyToFlux(GitHubRepository.class)
                .filter(repo -> !repo.isFork())  // Filter non-fork repositories
                .flatMap(repo -> {
                    // Fetch branches for each repo
                    return getRepositoryBranches(username, repo.getName())
                            .map(branches -> new GitHubRepositoryResponse(repo.getName(),
                                    repo.getOwner().getLogin(), branches));
                })
                .collectList();
    }

    private Mono<List<BranchResponse>> getRepositoryBranches(String username, String repoName) {
        return gitHubWebClient.get()
                .uri("/repos/{username}/{repoName}/branches", username, repoName)
                .retrieve()
                .bodyToFlux(Branch.class)
                .map(branch -> new BranchResponse(branch.getName(), branch.getCommit().getSha()))
                .collectList();
    }
}
