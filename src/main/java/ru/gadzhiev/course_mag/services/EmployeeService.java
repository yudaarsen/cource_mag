package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.Employee;

import java.util.List;

public interface EmployeeService {

    Employee create(final Employee employee) throws Exception;

    Employee update(final Employee employee) throws Exception;

    int delete(final Employee employee) throws Exception;

    List<Employee> findAllEmployees() throws Exception;

    Employee findById(final Employee employee) throws Exception;

}
