package ru.gadzhiev.course_mag.controllers.catalogs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gadzhiev.course_mag.controllers.exceptions.RestApiException;
import ru.gadzhiev.course_mag.models.Function;
import ru.gadzhiev.course_mag.models.validations.FunctionValidation;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class FunctionController {

    private final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @PostMapping(path = "/function")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Function createFunction(@RequestBody @Validated(value = FunctionValidation.class) final Function function) throws RestApiException {
        return null;
    }

    @PatchMapping(path = "/function/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Function updateFunction(@PathVariable("id") @NotNull @Min(1) int id,
                                   @RequestBody @Validated final Function function) throws RestApiException {
        return null;
    }

    @DeleteMapping(path = "/function/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteFunction(@PathVariable("id") @NotNull @Min(1) int id) throws RestApiException {

    }

    @GetMapping(path = "/functions")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Function> getFunctionsForDepartment(@RequestParam("department_id") @NotNull @Min(1) int departmentId) throws RestApiException {
        return null;
    }
}
