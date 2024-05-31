package com.hitesh.runnerz.run;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class RunControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RunRepository repo;
    @MockBean
    JDBCClientRunRepository jdbcRepo;

    private final List<Run> runs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        runs.add(new Run(1, "First Test run", LocalDateTime.now(),
                LocalDateTime.now().plus(1, ChronoUnit.HOURS), 91,
                Location.OUTDOOR, 0));
        runs.add(new Run(2, "Second Test run", LocalDateTime.now(),
                LocalDateTime.now().plus(1, ChronoUnit.HOURS), 51,
                Location.OUTDOOR, 0));
    }

    @Test
    void shouldFindAll() throws Exception {
        when(repo.findAll()).thenReturn(runs);

        mvc.perform(MockMvcRequestBuilders.get("/api/runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(runs.size())));
    }

    @Test
    void shouldFindById() throws Exception {
        Run run = runs.get(0);
        when(repo.findById(1)).thenReturn(Optional.of(run));

        mvc.perform(MockMvcRequestBuilders.get("/api/runs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(run.id())))
                .andExpect(jsonPath("$.title", is(run.title())))
                .andExpect(jsonPath("$.distance", is(run.distance())));
    }

    @Test
    void shouldNotFindById() throws Exception {

        when(repo.findById(99)).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/api/runs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewRun() throws Exception {
        Run run = new Run(3, "Second Test run", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 51,
                Location.OUTDOOR, 0);

        mvc.perform(MockMvcRequestBuilders.post("/api/runs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(run)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateRun() throws Exception {
        Run run = new Run(1, "First121w Test run", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS),
                1,
                Location.INDOOR, 0);

        mvc.perform(MockMvcRequestBuilders.put("/api/runs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(run)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteRun() throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete("/api/runs/1"))
                .andExpect(status().isNoContent());
    }

}
