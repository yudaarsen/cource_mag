package ru.gadzhiev.course_mag.controllers.catalogs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gadzhiev.course_mag.controllers.exceptions.RestApiException;
import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.Function;
import ru.gadzhiev.course_mag.models.validations.FunctionValidation;
import ru.gadzhiev.course_mag.services.FunctionService;

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

    @Autowired
    private FunctionService functionService;

    @PostMapping(path = "/function")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Function createFunction(@RequestBody @Validated(value = FunctionValidation.class) final Function function) throws RestApiException {
        try {
            Function createdFunction = functionService.create(function);
            logger.debug("Function is created: " + createdFunction.id());
            return createdFunction;
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23503")) {
                    logger.warn("Department does not exist: " + function.name() + " " + function.department().id());
                    throw new RestApiException(HttpStatus.BAD_REQUEST, "Department does not exist");
                }
                if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                    logger.warn("Function with specified name already exists in department: " + function.name() + " " + function.department().id());
                    throw new RestApiException(HttpStatus.BAD_REQUEST, "Function with specified name already exists in department");
                }
            }
            logger.error("Error while creating function", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating function");
        }
    }

    @PutMapping(path = "/function/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Function updateFunction(@PathVariable("id") @NotNull @Min(1) int id,
                                   @RequestBody @Validated final Function function) throws RestApiException {
        try {
            Function updatedFunction = functionService.update(new Function(id, function.name(), null));
            logger.debug("Function is created: " + updatedFunction.id());
            return updatedFunction;
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                    logger.warn("Function with specified name already exists in department: " + function.name());
                    throw new RestApiException(HttpStatus.BAD_REQUEST, "Function with specified name already exists in department");
                }
            }
            logger.error("Error while updating function", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating function");
        }
    }

    @DeleteMapping(path = "/function/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteFunction(@PathVariable("id") @NotNull @Min(1) int id) throws RestApiException {
        try {
            functionService.delete(new Function(id, null, null));
            logger.debug("Function is deleted: " + id);
        }
        catch (IllegalArgumentException e) {
            logger.debug(e.getMessage());
            throw new RestApiException(HttpStatus.NO_CONTENT, "");
        }
        catch (Exception e) {
            logger.error("Error while deleting function", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting function");
        }
    }

    @GetMapping(path = "/functions")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Function> getFunctionsForDepartment(@RequestParam("department") @NotNull @Min(1) int departmentId) throws RestApiException {
        try {
            return functionService.findAllForDepartment(new Department(departmentId, null));
        } catch (Exception e) {
            logger.error("Error while getting functions for dep: " + departmentId , e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting function");
        }
    }
}
