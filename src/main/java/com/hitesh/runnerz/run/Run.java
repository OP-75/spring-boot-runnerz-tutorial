package com.hitesh.runnerz.run;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Run(
        @Positive Integer id,
        @NotEmpty @NotNull String title,
        LocalDateTime startedOn,
        LocalDateTime endedOn,
        @Positive Integer distance,
        Location location) {

    public Run {

        if (endedOn.isBefore(startedOn)) {
            throw new IllegalArgumentException("endedOn must be AFTER startedOn");
        }

    }

}
