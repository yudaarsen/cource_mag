package ru.gadzhiev.course_mag.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import org.springframework.lang.Nullable;
import ru.gadzhiev.course_mag.models.validations.FunctionValidation;

public record Function(
        int id,
        @NotNull(groups = { FunctionValidation.class, Default.class })
        @NotBlank(groups = { FunctionValidation.class, Default.class })
        @Size(min = 1, max = 100, groups = { FunctionValidation.class, Default.class })
        String name,

        @NotNull(groups = FunctionValidation.class)
        // ConstructorMapper is trying to map "department" column to record, but this column does not exist
        // Then specify @Nullable to avoid an error
        @Nullable
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Valid
        Department department)
{}
