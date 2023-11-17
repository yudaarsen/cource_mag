package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.Department;

import java.util.List;

public interface DepartmentService {

    Department create(final Department department) throws Exception;
    Department update(final Department department) throws Exception;
    List<Department> findAll() throws Exception;
    int delete(final Department department) throws Exception;

}
