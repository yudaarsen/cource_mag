package ru.gadzhiev.course_mag.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.springframework.lang.Nullable;
import ru.gadzhiev.course_mag.models.validations.EmployeeValidation;
import ru.gadzhiev.course_mag.models.validations.EmployeeValidationUpdate;

public record Employee(
        @Min(value = 1, groups = { EmployeeValidation.class, Default.class })
        int personnelNumber,
        @NotNull(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @NotBlank(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @Size(max = 100, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        String firstName,
        @NotNull(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @NotBlank(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @Size(max = 100, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        String lastName,
        @Size(max = 100, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        String middleName,
        @NotNull(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @Valid
        @Nullable
        Function function,
        @Email(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        String email,
        @Size(max = 15, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        String phone,
        @Min(value = 1, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        long salary
)
{}
