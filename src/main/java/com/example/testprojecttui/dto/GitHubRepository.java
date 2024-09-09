package com.example.testprojecttui.dto;

import lombok.Data;

@Data
public class GitHubRepository {
    private String name;
    private GitHubOwner owner;
    private boolean fork;
}
