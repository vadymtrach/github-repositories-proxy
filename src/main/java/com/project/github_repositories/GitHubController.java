package com.project.github_repositories;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GitHubController {
    private final GitHubService service;
    GitHubController(GitHubService service){
        this.service = service;
    }

    @GetMapping("/users/{username}/repos")
    public List<RepoResponse> getRepos(@PathVariable String username){
        return service.getRepositories(username);
    }
}
