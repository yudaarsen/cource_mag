package ru.gadzhiev.course_mag.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.springframework.lang.Nullable;
import ru.gadzhiev.course_mag.models.validations.DeductionValidation;

public record Deduction (
        @NotNull(groups = { DeductionValidation.class, Default.class })
        @Size(min = 4, max = 4, groups = { DeductionValidation.class, Default.class })
        @NotBlank(groups = { DeductionValidation.class, Default.class })
        String code,
        @NotNull(groups = { DeductionValidation.class })
        @Nullable
        @Valid
        Account account,
        @Min(value = 0, groups = { DeductionValidation.class, Default.class })
        @Max(value = 100, groups = { DeductionValidation.class, Default.class })
        int rate
)
{}
