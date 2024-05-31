package com.hitesh.runnerz.run;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Run(
        @Positive @Id Integer id,
        @NotEmpty @NotNull String title,
        LocalDateTime startedOn,
        LocalDateTime endedOn,
        @Positive Integer distance,
        Location location,
        @Version Integer version // this is for Spring Data interface
) {

    public Run {

        if (endedOn.isBefore(startedOn)) {
            throw new IllegalArgumentException("endedOn must be AFTER startedOn");
        }

    }

}
