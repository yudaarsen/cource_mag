package ru.gadzhiev.course_mag.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.gadzhiev.course_mag.models.validations.DocumentValidation;

import java.util.Date;
import java.util.List;

public record Document(
        int id,
        @Valid
        DocumentType documentType,
        @NotNull(groups = { DocumentValidation.class })
        Date postingDate,
        @Size(max = 250, groups = { DocumentValidation.class })
        String note,
        @NotNull(groups = { DocumentValidation.class })
        @Valid
        List<DocumentPosition> documentPositions
)
{}
