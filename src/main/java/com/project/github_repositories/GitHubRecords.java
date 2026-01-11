package com.project.github_repositories;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

record GitHubRepo(String name,
                  boolean fork,
                  Owner owner,
                  @JsonProperty("html_url") String htmlUrl) { // Poprawka
    record Owner(String login){}
}

record GitHubBranch(String name, Commit commit){
    record Commit(String sha,
                  String url){}
}

record RepoResponse(String name,
                    String ownerLogin,
                    List<BranchResponse> branches){}

record BranchResponse(String name,
                      String sha){}

record ApiError(int status,
                String message){}