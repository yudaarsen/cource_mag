package ru.gadzhiev.course_mag.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.springframework.lang.Nullable;
import ru.gadzhiev.course_mag.models.validations.DeductionValidation;
import ru.gadzhiev.course_mag.models.validations.EmployeeDeductionValidation;

public record Deduction (
        @NotNull(groups = { DeductionValidation.class, Default.class, EmployeeDeductionValidation.class })
        @Size(min = 4, max = 4, groups = { DeductionValidation.class, Default.class, EmployeeDeductionValidation.class })
        @NotBlank(groups = { DeductionValidation.class, Default.class, EmployeeDeductionValidation.class })
        String code,
        @NotNull(groups = { DeductionValidation.class })
        @Nullable
        @Valid
        Account account,
        @Min(value = 0, groups = { DeductionValidation.class, Default.class, EmployeeDeductionValidation.class })
        @Max(value = 10000, groups = { DeductionValidation.class, Default.class, EmployeeDeductionValidation.class })
        int rate // В базисных пунктах
)
{}
