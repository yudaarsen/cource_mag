package ru.gadzhiev.course_mag.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.gadzhiev.course_mag.models.validations.FunctionValidation;

public record Function(
        int id,
        @Size(min = 1, max = 100)
        String name,
        @NotNull(groups = FunctionValidation.class)
        @Valid
        Department department)
{}
