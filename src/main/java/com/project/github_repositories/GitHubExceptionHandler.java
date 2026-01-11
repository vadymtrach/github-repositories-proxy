package com.project.github_repositories;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GitHubExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    ResponseEntity<ApiError> notFound(HttpClientErrorException.NotFound ex) {
        ApiError body = new ApiError(HttpStatus.NOT_FOUND.value(), "GitHub user not found");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }
}
