package ru.gadzhiev.course_mag.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.springframework.lang.Nullable;
import ru.gadzhiev.course_mag.models.validations.DocumentValidation;
import ru.gadzhiev.course_mag.models.validations.EmployeeValidation;
import ru.gadzhiev.course_mag.models.validations.EmployeeValidationUpdate;

public record Employee(
        @Min(value = 1, groups = { EmployeeValidation.class, Default.class, DocumentValidation.class })
        int personnelNumber,
        @NotNull(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @NotBlank(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @Size(max = 100, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String firstName,
        @NotNull(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @NotBlank(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @Size(max = 100, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String lastName,
        @Size(max = 100, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String middleName,
        @NotNull(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @Valid
        @Nullable
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Function function,
        @Email(groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String email,
        @Size(max = 15, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String phone,
        @Min(value = 1, groups = { EmployeeValidationUpdate.class, EmployeeValidation.class, Default.class })
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        long salary
)
{}
