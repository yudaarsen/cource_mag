package ru.gadzhiev.course_mag.controllers.catalogs;

import jakarta.validation.Valid;
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
import ru.gadzhiev.course_mag.models.validations.FunctionValidation;
import ru.gadzhiev.course_mag.services.DepartmentService;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class DepartmentController {

    private final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @PostMapping(path = "/department")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Department postDepartment(@RequestBody @Validated final Department department) throws RestApiException {
        try {
            Department createdDepartment = departmentService.create(department);
            logger.debug("New department is created: " + createdDepartment);
            return createdDepartment;
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                    logger.warn("Department name already exists: " + department.name());
                    throw new RestApiException(HttpStatus.CONFLICT, "Department name already exists");
                }
            }
            logger.error("Error while creating department", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating department");
        }
    }

    @PatchMapping(path = "/department/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Department patchDepartment(@PathVariable("id") @NotNull @Min(1) int id, @RequestBody @Validated Department department) throws RestApiException {
        try {
            Department updatedDepartment = departmentService.update(new Department(id, department.name()));
            logger.debug("Department is updated: " + updatedDepartment);
            return updatedDepartment;
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {
                logger.warn(e.getMessage());
                throw new RestApiException(HttpStatus.NO_CONTENT, "");
            }
            logger.error("Error while updating department", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating department");
        }
    }

    @GetMapping(path = "/departments")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Department> getAllDepartments() throws RestApiException {
        try {
            return departmentService.findAll();
        } catch (Exception e) {
            logger.error("Error while updating department", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting department");
        }
    }

    @DeleteMapping(path = "/department/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteDepartment(@PathVariable("id") @Min(1) int id) throws RestApiException {
        try {
            departmentService.delete(new Department(id, ""));
            logger.debug("Department is deleted: " + id);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {
                logger.warn(e.getMessage());
                throw new RestApiException(HttpStatus.NO_CONTENT, "");
            }
            logger.error("Error while deleting department", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting department");
        }
    }
}
