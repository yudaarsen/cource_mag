package ru.gadzhiev.course_mag.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.gadzhiev.course_mag.models.validations.DocumentValidation;

public record DocumentPosition(
        @JsonIgnore
        int documentId,
        int posNum,
        char posType,
        @Valid
        @NotNull(groups = { DocumentValidation.class })
        Account account,
        @Min(value = 1, groups = { DocumentValidation.class })
        long amount,
        @Valid
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Employee employee,
        @Size(max = 250, groups = { DocumentValidation.class })
        String note
)
{
        public static final char TYPE_DEBIT = 'D';
        public static final char TYPE_CREDIT = 'C';

}
