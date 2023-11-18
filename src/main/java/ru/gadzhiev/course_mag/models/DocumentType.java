package ru.gadzhiev.course_mag.models;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import ru.gadzhiev.course_mag.models.validations.DocumentTypeValidation;

public record DocumentType (
    @NotNull(groups = { DocumentTypeValidation.class, Default.class })
    @NotBlank(groups = { DocumentTypeValidation.class, Default.class })
    @Size(min = 1, max = 4, groups = { DocumentTypeValidation.class, Default.class })
    String code,
    @NotNull(groups = { DocumentTypeValidation.class })
    @NotBlank(groups = { DocumentTypeValidation.class })
    @Size(max = 50, groups = { DocumentTypeValidation.class })
    String name
)
{}
