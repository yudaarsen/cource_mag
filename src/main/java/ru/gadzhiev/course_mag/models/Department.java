package ru.gadzhiev.course_mag.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.gadzhiev.course_mag.models.validations.FunctionValidation;

public record Department(
        @NotNull(groups = FunctionValidation.class)
        @Min(value = 1, groups = FunctionValidation.class)
        int id,
        @NotNull @Size(min = 1, max = 50)
        String name
)
{}
