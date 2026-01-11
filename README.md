# GitHub Repositories Proxy
Spring Boot application that exposes an API
to list non-fork GitHub repositories for a given GitHub user
## Features
* Lists all repositories for a specific user
* Filters out forks
* Aggregates branch names and last commit SHA for each repository
* Handles non-existent users with a 404 JSON error structure
## Tech Stack
* Java 25
* Spring Boot 4.0.1
* Gradle (Kotlin DSL)
* Wiremock (Integration Testing)
## Build and Run
### Run Application
To start the server on port 8080:
```bash
./gradlew bootRun
```
### Run tests
To execute integration tests using WireMock:
```bash
./gradlew test
```
# API Documentation
## Get user repositories
### Request: GET /users/{username}/repos

Example: GET http://localhost:8080/users/octocat/repos

Success Response (200 OK):
```json
[
  {
    "name": "git-consortium",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "master",
        "sha": "b33a9c7c02ad93f621fa38f0e9fc9e867e12fa0e"
      }
    ]
  }
]
```
Error Response (404 Not Found): if the user does not exist on GitHub:
```json
{
    "status": 404,
    "message": "GitHub user not found"
}
```