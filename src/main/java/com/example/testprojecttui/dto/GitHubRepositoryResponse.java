package com.example.testprojecttui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class GitHubRepositoryResponse {
    private String repositoryName;
    private String ownerLogin;
    private List<BranchResponse> branches;

}
