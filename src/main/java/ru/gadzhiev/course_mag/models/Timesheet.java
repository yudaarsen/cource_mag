package ru.gadzhiev.course_mag.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.lang.Nullable;

public record Timesheet(
        @Nullable
        @JsonIgnore
        Employee employee,
        @Min(value = 1991)
        @Max(value = 9999)
        int year,
        @Min(value = 1)
        @Max(value = 12)
        int month,
        @Min(value = 1)
        @Max(value = 31)
        int day,
        boolean present
)
{}
