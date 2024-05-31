package com.hitesh.runnerz.run;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JDBCClientRunRepository.class)
public class JDBCClientRunRepositoryTest {

    @Autowired
    JDBCClientRunRepository repo;

    @BeforeEach
    void setUp() {

        repo.save(new Run(1, "First Test run", LocalDateTime.now(),
                LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5,
                Location.OUTDOOR, 0));
        repo.save(new Run(2, "Second Test run", LocalDateTime.now(),
                LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5,
                Location.OUTDOOR, 0));

    }

    @Test
    void shouldFindAll() {
        List<Run> runs = repo.findAll();
        // this will fail if number of rows/runs in the postgres db != 2
        System.out.println(runs.toString());
        assertEquals(2, runs.size(), "Should find all runs");
    }

    @Test
    void shouldFindRunWithValidId() {
        var run = repo.findById(1).get();
        assertEquals("First Test run", run.title());
        assertEquals(5, run.distance());
    }

    @Test
    void shouldNotFindRunWithInvalidId() {
        var run = repo.findById(3);
        assertTrue(run.isEmpty());
    }

    @Test
    void shouldCreateNewRun() {
        repo.save(new Run(3,
                "Friday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR,
                0));
        List<Run> runs = repo.findAll();
        assertEquals(3, runs.size());
        repo.delete(3);
    }

    @Test
    void shouldUpdateRun() {
        repo.update(1, new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                1555,
                Location.OUTDOOR, 0));
        var run = repo.findById(1).get();
        assertEquals("Monday Morning Run", run.title());
        assertEquals(1555, run.distance());
        assertEquals(Location.OUTDOOR, run.location());
    }

    @Test
    void shouldDeleteRun() {
        repo.delete(1);
        List<Run> runs = repo.findAll();
        assertEquals(1, runs.size());

        // restore the run in DB
        repo.save(new Run(1, "First Test run", LocalDateTime.now(),
                LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5,
                Location.OUTDOOR, 0));
    }

    @AfterEach
    void destroy() {
        repo.delete(1);
        repo.delete(2);

    }
}
