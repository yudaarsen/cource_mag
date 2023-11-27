package ru.gadzhiev.course_mag.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import ru.gadzhiev.course_mag.models.validations.DocumentTypeValidation;
import ru.gadzhiev.course_mag.models.validations.DocumentValidation;

public record DocumentType (
    @NotNull(groups = { DocumentTypeValidation.class, Default.class, DocumentValidation.class })
    @NotBlank(groups = { DocumentTypeValidation.class, Default.class, DocumentValidation.class })
    @Size(min = 4, max = 4, groups = { DocumentTypeValidation.class, Default.class, DocumentValidation.class })
    String code,
    @NotNull(groups = { DocumentTypeValidation.class })
    @NotBlank(groups = { DocumentTypeValidation.class })
    @Size(max = 50, groups = { DocumentTypeValidation.class })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String name
)
{}
