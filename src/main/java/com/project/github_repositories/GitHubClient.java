package com.project.github_repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GitHubClient {
    private final RestClient client;

    GitHubClient(RestClient.Builder builder, @Value("${github.api.url}") String baseUrl){
        client = builder.baseUrl(baseUrl)
                .defaultHeader("User-Agent", "List GitHub Repositories")
                .build();
    }

    List<GitHubRepo> getRepos(String username){
        return client.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
    List<GitHubBranch> getBranches(String username, GitHubRepo repo){
        return client.get()
                .uri("repos/{username}/{repo}/branches", username, repo.name())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
