package com.hitesh.runnerz.run;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // ! IMP since the test run are designed to be run in a particular
                                                      // ! order
public class RunControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    RestClient restClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JDBCClientRunRepository jdbcClientRunRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);
    }

    @Test
    @Order(1)
    void shouldCreateRun() throws JsonProcessingException {
        Run run = new Run(3, "Third Test run", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 51,
                Location.OUTDOOR, null);

        ResponseEntity<Void> response = restClient.post()
                .uri("/api/runs")
                .contentType(MediaType.APPLICATION_JSON)
                .body(run)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    @Order(2)
    void shouldFindById() {
        ResponseEntity<Run> runFromDb = restClient.get()
                .uri("/api/runs/3")
                .retrieve()
                .toEntity(Run.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, runFromDb.getStatusCode()),
                () -> assertEquals(3, runFromDb.getBody().id()),
                () -> assertEquals("Third Test run", runFromDb.getBody().title()));
    }

    @Test
    @Order(3)
    void shouldUpdateRun() throws JsonProcessingException {
        Run run = new Run(3, "Third Updated Test run", LocalDateTime.now(),
                LocalDateTime.now().plus(1, ChronoUnit.HOURS), 1,
                Location.OUTDOOR, null);

        ResponseEntity<Void> newRun = restClient.put()
                .uri("/api/runs/3")
                .body(run)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, newRun.getStatusCode());

    }

    @Test
    @Order(4)
    void shouldFindUpdatedRun() {
        ResponseEntity<Run> runFromDb = restClient.get()
                .uri("/api/runs/3")
                .retrieve()
                .toEntity(Run.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, runFromDb.getStatusCode()),
                () -> assertEquals(3, runFromDb.getBody().id()),
                () -> assertEquals("Third Updated Test run", runFromDb.getBody().title()),
                () -> assertEquals(1, runFromDb.getBody().distance()));
    }

    @Test
    @Order(5)
    void shouldFind1Runs() {
        List<Run> runs = restClient.get()
                .uri("/api/runs")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Run>>() {
                });

        assertEquals(1, runs.size()); // expected value can change since we are doing test on DB
    }

    @Test
    @Order(6)
    void shouldDeleteRun() {
        ResponseEntity<Void> response = restClient.delete()
                .uri("/api/runs/3")
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(7)
    void shouldFind0Runs() {
        List<Run> runs = restClient.get()
                .uri("/api/runs")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Run>>() {
                });

        assertEquals(0, runs.size()); // expected value can change since we are doing test on DB
    }

}
