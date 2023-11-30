package ru.gadzhiev.course_mag.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.gadzhiev.course_mag.models.validations.DocumentValidation;

import java.text.DecimalFormat;

public record DocumentPosition(
        @JsonIgnore
        int documentId,
        int posNum,
        char posType,
        @Valid
        @NotNull(groups = { DocumentValidation.class })
        Account account,
        @Min(value = 1, groups = { DocumentValidation.class })
        double amount,
        @Valid
        Employee employee,
        @Size(max = 250, groups = { DocumentValidation.class })
        String note
)
{
        public static final char TYPE_DEBIT = 'D';
        public static final char TYPE_CREDIT = 'C';

        private static final DecimalFormat df = new DecimalFormat("0.00");

        @JsonGetter(value = "amount")
        public double getAmount() {
                return Double.parseDouble(df.format(amount));
        }
}
