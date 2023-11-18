package ru.gadzhiev.course_mag.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import org.springframework.lang.Nullable;
import ru.gadzhiev.course_mag.models.validations.AccountValidation;

public record Account(
        @NotNull(groups = { AccountValidation.class, Default.class })
        @NotBlank(groups = { AccountValidation.class, Default.class })
        @Digits(integer = 10, fraction = 0, groups = { AccountValidation.class, Default.class })
        @Size(min = 10, max = 10, groups = { AccountValidation.class, Default.class })
        String code,
        @NotNull
        @NotBlank
        @Size(max = 100, groups = { AccountValidation.class, Default.class })
        String name,
        @Nullable
        @Valid
        @JsonSetter(value = "parent")
        Account parent
)
{
    @JsonGetter(value = "parent")
    public String getJsonParent() {
        return parent == null ? null : parent.code();
    }
}
