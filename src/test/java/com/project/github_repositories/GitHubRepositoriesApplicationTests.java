package com.project.github_repositories;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubRepositoriesApplicationTests {
    static WireMockServer wireMock;

    @LocalServerPort
    int port;

    RestTemplate restTemplate = new RestTemplate();

    @BeforeAll
    static void startWireMock() {
        wireMock = new WireMockServer(0);
        wireMock.start();

        configureFor("localhost", wireMock.port());

        stubFor(get("/users/template_username/repos")
                .willReturn(okJson("""
                    [
                      { "name": "repo-1", "fork": false, "owner": { "login": "template_username" } },
                      { "name": "repo-2", "fork": true, "owner": { "login": "template_username" } }
                    ]
                """)));

        stubFor(get("/repos/template_username/repo-1/branches")
                .willReturn(okJson("""
                    [
                      { "name": "master", "commit": { "sha": "12325427637ae3f", "url": "x" } }
                    ]
                """)));

        stubFor(get("/users/missing/repos")
                .willReturn(notFound().withBody("{" +
                        "\"status\":404," +
                        "\"message\":\"GitHub user not found\"}")));
    }

    @AfterAll
    static void stopWireMock() {
        wireMock.stop();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("github.api.url",
                () -> "http://localhost:" + wireMock.port());
    }

    @BeforeEach
    void configureRestTemplate() {
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });
    }

    @BeforeEach
    void resetWireMock(){
        wireMock.resetRequests();
    }

    @Test
    void shouldReturnOnlyNonForkRepos() {
        String response = restTemplate.getForObject(
                "http://localhost:" + port + "/users/template_username/repos",
                String.class
        );

        assertThat(response).contains("repo-1");
        assertThat(response).contains("12325427637ae3f");
        assertThat(response).doesNotContain("repo-2");
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() {
        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/users/missing/repos",
                String.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).contains("GitHub user not found");
    }
}