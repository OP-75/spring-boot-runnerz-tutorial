package com.hitesh.runnerz.run;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PostConstruct;

@Repository
public class RunRepository {

    private final List<Run> runs = new ArrayList<>();

    @PostConstruct
    private void init() {
        runs.add(new Run(1, "First run", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5,
                Location.OUTDOOR));
        runs.add(new Run(2, "second run", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5,
                Location.OUTDOOR));
    }

    List<Run> finalAll() {
        return runs;
    }

    public Optional<Run> findById(int id) {

        // ! Remember u can access run.id field directly, u need to call getter run.id()
        return runs.stream()
                .filter(run -> run.id() == id)
                .findFirst();

    }

    void create(Run run) {
        runs.add(run);
    }

    public void update(Integer id, Run newRun) {
        Optional<Run> oldRun = findById(id);
        if (oldRun.isPresent()) {
            runs.set(runs.indexOf(oldRun.get()), newRun);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void delete(Integer id) {
        Optional<Run> oldRun = findById(id);
        if (oldRun.isPresent()) {
            runs.remove(runs.indexOf(oldRun.get()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
