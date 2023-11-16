package ru.gadzhiev.course_mag.controllers.catalogs;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.responses.Response;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class DepartmentController {

    @PostMapping(path = "/department")
    
    public Response postDepartment(@RequestBody @Valid final Department department, final HttpServletResponse response) {

        return new Response(HttpStatus.CREATED);
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
    public Response deleteDepartment(@PathVariable("id") @NotNull @Min(1) int id) {

        return new Response(HttpStatus.CREATED);
    }
}
