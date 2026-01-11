package com.project.github_repositories;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubService {
    private final GitHubClient client;

    GitHubService(GitHubClient client){
        this.client = client;
    }

    List<RepoResponse> getRepositories(String username){
        List<GitHubRepo> allRepos = client.getRepos(username);
        if (allRepos == null) return List.of();
        return allRepos.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    List<GitHubBranch> branches = client.getBranches(username, repo);
                    return mappingToResponse(repo, branches);
                })
                .toList();

    }

    private RepoResponse mappingToResponse(GitHubRepo repo, List<GitHubBranch> branches){
        List<BranchResponse> branchResponses = (branches == null) ? List.of() :
                branches.stream()
                        .map(branch -> new BranchResponse(branch.name(), branch.commit().sha()))
                        .toList();
        return new RepoResponse(repo.name(), repo.owner().login(), branchResponses);
    }
}
