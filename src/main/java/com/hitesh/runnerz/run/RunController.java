package com.hitesh.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/runs") // all Get, put,post,delete methods have initial url "/api/runs"
public class RunController {

    private final RunRepository runRepository;
    private final JDBCClientRunRepository jdbcClientRunRepository;

    public RunController(RunRepository runRepository, JDBCClientRunRepository jdbcClientRunRepository) {
        this.runRepository = runRepository;
        this.jdbcClientRunRepository = jdbcClientRunRepository;
    }

    @GetMapping("")
    List<Run> findAll() {
        return runRepository.findAll();
    }

    @GetMapping("/{id}")
    public Run findById(@PathVariable Integer id) {
        Optional<Run> run = runRepository.findById(id);
        if (run.isPresent()) {
            return run.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // post
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Run run) {
        runRepository.save(run); // this uses Spring Data Interface
    }

    // update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody Run newRun) {
        // runRepository.update(id, newRun); //this uses JDBCClientRunRepository

        // throw new IllegalAccessError("Method not implemented in RestController.java &
        // RunRepository");

        jdbcClientRunRepository.update(id, newRun);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        runRepository.deleteById(id); // this uses Spring Data Interface
    }

}
