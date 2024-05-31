package com.hitesh.runnerz.run;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.ListCrudRepository;

@Repository
public interface RunRepository extends ListCrudRepository<Run, Integer> {

}
