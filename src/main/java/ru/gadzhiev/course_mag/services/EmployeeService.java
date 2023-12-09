package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.Deduction;
import ru.gadzhiev.course_mag.models.Employee;
import ru.gadzhiev.course_mag.models.EmployeeDeduction;
import ru.gadzhiev.course_mag.models.Timesheet;

import java.util.List;

public interface EmployeeService {

    Employee create(final Employee employee) throws Exception;

    Employee update(final Employee employee) throws Exception;

    int delete(final Employee employee) throws Exception;

    List<Employee> findAllEmployees() throws Exception;

    Employee findById(final Employee employee) throws Exception;

    Deduction addEmployeeDeduction(final EmployeeDeduction employeeDeduction) throws Exception;

    int deleteEmployeeDeduction(final EmployeeDeduction employeeDeduction) throws Exception;

    List<Deduction> findEmployeeDeductions(final Employee employee) throws Exception;

    Timesheet createTimesheet(final Timesheet timesheet) throws Exception;

    int deleteTimesheet(final Timesheet timesheet) throws Exception;

    List<Timesheet> findEmployeeTimesheet(final Employee employee) throws Exception;
}
