package ru.gadzhiev.course_mag.controllers.catalogs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gadzhiev.course_mag.controllers.exceptions.RestApiException;
import ru.gadzhiev.course_mag.models.Deduction;
import ru.gadzhiev.course_mag.models.Employee;
import ru.gadzhiev.course_mag.models.EmployeeDeduction;
import ru.gadzhiev.course_mag.models.validations.EmployeeDeductionValidation;
import ru.gadzhiev.course_mag.models.validations.EmployeeValidation;
import ru.gadzhiev.course_mag.models.validations.EmployeeValidationUpdate;
import ru.gadzhiev.course_mag.services.EmployeeService;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class EmployeeController {

    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(path = "/employee")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody @Validated(value = EmployeeValidation.class) final Employee employee) throws RestApiException {
        try {
            Employee createdEmployee = employeeService.create(employee);
            logger.debug("Employee is created: " + createdEmployee.personnelNumber());
            return createdEmployee;
        }
        catch (IllegalArgumentException e) {
            logger.warn("Function does not exist: " + employee.function().id());
            throw new RestApiException(HttpStatus.BAD_REQUEST, "Function does not exist");
        }
        catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                    String constraint = ((PSQLException)e.getCause()).getServerErrorMessage().getConstraint();
                    if(constraint != null) {
                        if(constraint.equals("employee_pkey")) {
                            logger.warn("Employee with personnel number already exists: " + employee.personnelNumber());
                            throw new RestApiException(HttpStatus.CONFLICT, "Employee with personnel number already exists");
                        } else if (constraint.equals("employee_email_key")) {
                            logger.warn("Employee with email already exists: " + employee.email());
                            throw new RestApiException(HttpStatus.CONFLICT, "Employee with email already exists");
                        } else if (constraint.equals("employee_phone_key")) {
                            logger.warn("Employee with phone already exists: " + employee.phone());
                            throw new RestApiException(HttpStatus.CONFLICT, "Employee with phone already exists");
                        }
                    }
                }
            }
            logger.error("Error while creating employee", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating employee");
        }
    }

    @PutMapping(path = "/employee/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Employee updateEmployee(@PathVariable("id") @Min(1) int personnelNumber,
                                   @RequestBody @Validated(value = EmployeeValidationUpdate.class) final Employee employee) throws RestApiException {
        try {
            Employee updatedEmployee = employeeService.update(new Employee(
                    personnelNumber,
                    employee.firstName(),
                    employee.lastName(),
                    employee.middleName(),
                    employee.function(),
                    employee.email(),
                    employee.phone(),
                    employee.salary()
            ));
            logger.debug("Employee is updated: " + updatedEmployee.personnelNumber());
            return updatedEmployee;
        }
        catch (IllegalArgumentException e) {
            logger.warn("Function does not exist: " + employee.function().id());
            throw new RestApiException(HttpStatus.BAD_REQUEST, "Function does not exist");
        }
        catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                    String constraint = ((PSQLException)e.getCause()).getServerErrorMessage().getConstraint();
                    if(constraint != null) {
                        if (constraint.equals("employee_email_key")) {
                            logger.warn("Employee with email already exists: " + employee.email());
                            throw new RestApiException(HttpStatus.CONFLICT, "Employee with email already exists");
                        } else if (constraint.equals("employee_phone_key")) {
                            logger.warn("Employee with phone already exists: " + employee.phone());
                            throw new RestApiException(HttpStatus.CONFLICT, "Employee with phone already exists");
                        }
                    }
                }
            }
            logger.error("Error while updating employee", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating employee");
        }
    }

    @DeleteMapping(path = "/employee/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteEmployee(@PathVariable("id") @Min(1) int personnelNumber) throws RestApiException {
        try {
            employeeService.delete(new Employee(personnelNumber, null, null, null, null, null, null, 0));
            logger.debug("Employee is deleted: " + personnelNumber);
        } catch (IllegalArgumentException e) {
            logger.debug(e.getMessage());
            throw new RestApiException(HttpStatus.NO_CONTENT, "");
        } catch (Exception e) {
            logger.error("Error while deleting employee", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting employee");
        }
    }

    @GetMapping(path = "/employees")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Employee> getAllEmployees() throws RestApiException {
        try {
            return employeeService.findAllEmployees();
        } catch (Exception e) {
            logger.error("Error while getting employees", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting employees");
        }
    }

    @GetMapping(path = "/employee/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Employee getEmployee(@PathVariable("id") @Min(1) final int personnelNumber) throws RestApiException {
        try {
            return employeeService.findById(
                    new Employee(personnelNumber, null, null, null, null, null, null, 0)
            );
        } catch (IllegalArgumentException e) {
            logger.debug(e.getMessage());
            throw new RestApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Error while getting employees", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting employees");
        }
    }

    @PostMapping(path = "/employee/{id}/deduction")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Deduction addEmployeeDeduction(
            @PathVariable("id") @Min(1) final int personnelNumber,
            @RequestBody @Validated(value = EmployeeDeductionValidation.class) final Deduction employeeDeduction
    ) throws RestApiException {
        try {
            Deduction createdEmployeeDeduction = employeeService.addEmployeeDeduction(
                    new EmployeeDeduction(
                            new Employee(personnelNumber, null, null, null, null, null, null, 0),
                            employeeDeduction
                    )
            );
            logger.debug("Employee deduction is added: " + personnelNumber + " " + createdEmployeeDeduction.code() + " " + createdEmployeeDeduction.rate());
            return createdEmployeeDeduction;
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23503")) {
                    if(((PSQLException) e.getCause()).getServerErrorMessage().getConstraint().equals("employee_deduction_deduction_id_fkey")) {
                        logger.debug("Deduction does not exits: " + employeeDeduction.code());
                        throw new RestApiException(HttpStatus.BAD_REQUEST, "Deduction does not exits: " + employeeDeduction.code());
                    }
                    if(((PSQLException) e.getCause()).getServerErrorMessage().getConstraint().equals("employee_deduction_personnel_number_fkey")) {
                        logger.debug("Deduction does not exits: " + personnelNumber);
                        throw new RestApiException(HttpStatus.BAD_REQUEST, "Employee does not exits: " + personnelNumber);
                    }
                }
                if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                    logger.debug("Employee deduction already exists: " + personnelNumber + " " + employeeDeduction.code());
                    throw new RestApiException(HttpStatus.BAD_REQUEST, "Employee deduction already exists: " + personnelNumber + " " + employeeDeduction.code());
                }
            }
            logger.error("Error while adding employee deduction", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while adding employee deduction");
        }
    }

    @DeleteMapping(path = "/employee/{id}/deduction/{code}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteEmployeeDeduction(@PathVariable("id") @Min(1) final int personnelNumber,
                                        @PathVariable("code") @Size(min = 4, max = 4) @NotNull @NotBlank final String code) throws RestApiException {
        try {
            employeeService.deleteEmployeeDeduction(new EmployeeDeduction(
                    new Employee(personnelNumber, null, null, null, null, null, null, 0),
                    new Deduction(code, null, 0)
            ));
            logger.debug("Employee deduction is deleted: " +  personnelNumber + " " + code);
        } catch (IllegalArgumentException e) {
            logger.debug("Employee deduction does not exist: " + personnelNumber + " " + code);
            throw new RestApiException(HttpStatus.NO_CONTENT, "");
        } catch (Exception e) {
            logger.error("Error while deleting employee deduction", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting employee deduction");
        }
    }

    @GetMapping(path = "/employee/{id}/deductions")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Deduction> findEmployeeDeductions(@PathVariable("id") @Min(1) final int personnelNumber) throws RestApiException {
        try {
            return employeeService.findEmployeeDeductions(
                    new Employee(personnelNumber, null, null, null, null, null, null, 0)
            );
        } catch (Exception e) {
            logger.error("Error while getting employee deduction", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting employee deduction");
        }
    }
}
