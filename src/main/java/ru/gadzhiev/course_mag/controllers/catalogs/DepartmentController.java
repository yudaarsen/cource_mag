package ru.gadzhiev.course_mag.controllers.catalogs;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gadzhiev.course_mag.controllers.exceptions.RestApiException;
import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.responses.Response;
import ru.gadzhiev.course_mag.repositories.DepartmentRepository;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping(path = "/department")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postDepartment(@RequestBody @Valid final Department department) throws RestApiException {
        if(!departmentRepository.createDepartment(department)) {
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create department");
        }
    }

    @PatchMapping(path = "/department/{id}")
    public Response patchDepartment(@PathVariable("id") @NotNull @Min(1) int id, @RequestBody final Department department) {
        return new Response(HttpStatus.CREATED);
    }

    @GetMapping(path = "/departments")
    public Response getAllDepartments() {

        return new Response(HttpStatus.CREATED);
    }

    @GetMapping(path = "/department/{id}")
    public Response getDepartment(@PathVariable("id") @NotNull @Min(1) int id) {

        return new Response(HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/department/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteDepartment(@PathVariable("id") @NotNull @Min(1) int id) throws RestApiException {
        if(!departmentRepository.deleteDepartment(id)) {
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete department");
        }
    }
}
