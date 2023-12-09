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
import ru.gadzhiev.course_mag.models.validations.DeductionValidation;
import ru.gadzhiev.course_mag.models.validations.DocumentValidation;

public record Account(
        @NotNull(groups = { AccountValidation.class, Default.class, DeductionValidation.class, DocumentValidation.class })
        @NotBlank(groups = { AccountValidation.class, Default.class, DeductionValidation.class, DocumentValidation.class })
        @Digits(integer = 10, fraction = 0, groups = { AccountValidation.class, Default.class, DeductionValidation.class, DocumentValidation.class })
        @Size(min = 10, max = 10, groups = { AccountValidation.class, Default.class, DeductionValidation.class, DocumentValidation.class})
        String code,
        @NotNull
        @NotBlank
        @Size(max = 100, groups = { AccountValidation.class, Default.class })
        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        String name,
        @Nullable
        @Valid
        @JsonSetter(value = "parent")
        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        Account parent
)
{
    public static final String ACCOUNT_00 = "0000000000";
    public static final String ACCOUNT_20 = "2000000000";
    public static final String ACCOUNT_26 = "2600000000";
    public static final String ACCOUNT_51 = "5100000000";
    public static final String ACCOUNT_68 = "6800000000";
    public static final String ACCOUNT_69 = "6900000000";
    public static final String ACCOUNT_70 = "7000000000";

    public static final String ACCOUNT_76 = "7600000000";

    @JsonGetter(value = "parent")
    public String getJsonParent() {
        return parent == null ? null : parent.code();
    }
}
