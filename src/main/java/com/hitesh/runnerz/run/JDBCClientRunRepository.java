package com.hitesh.runnerz.run;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.hitesh.runnerz.RunnerzApplication;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@Repository
public class JDBCClientRunRepository {

    private static final Logger log = LoggerFactory.getLogger(RunnerzApplication.class);
    private final JdbcClient jdbcClient;

    public JDBCClientRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll() {
        return jdbcClient.sql("Select * from Run").query(Run.class).list();
    }

    public Optional<Run> findById(Integer id) {
        return jdbcClient.sql("Select * from Run WHERE id = ?").param(id).query(Run.class).optional();
    }

    public void save(@Valid Run run) {
        var updated = jdbcClient
                .sql("INSERT INTO Run (id, title, started_on, ended_on, distance, location, version) VALUES (?,?,?,?,?,?,?)")
                .params(List.of(run.id(), run.title(), run.startedOn(), run.endedOn(), run.distance(),
                        run.location().toString(), run.version()))
                .update();

        Assert.state(updated == 1, "Failed to create run with id = " + run.id());
    }

    public void update(Integer id, @Valid Run newRun) {
        var updated = jdbcClient
                .sql("UPDATE Run SET title = ?, started_on=?, ended_on=?, distance=?, location=? where id = ?")
                .params(List.of(newRun.title(), newRun.startedOn(), newRun.endedOn(), newRun.distance(),
                        newRun.location().toString(), id))
                .update();

        Assert.state(updated == 1, "Failed to update run with id = " + id);
    }

    public void delete(Integer id) {
        var updated = jdbcClient
                .sql("DELETE FROM Run where id = ?")
                .param(id)
                .update();

        Assert.state(updated == 1, "Failed to delete run with id = " + id);
    }

    public long count() {
        return jdbcClient.sql("Select count(*) from Run").query(Integer.class).single();
    }

    public void addAll(@Valid List<Run> runs) {
        for (Run run : runs) {
            save(run);
        }
    }

}
