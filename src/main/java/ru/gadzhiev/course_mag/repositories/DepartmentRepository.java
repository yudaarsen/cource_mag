package ru.gadzhiev.course_mag.repositories;

import ru.gadzhiev.course_mag.models.Department;

import java.util.List;

public interface DepartmentRepository {
    boolean createDepartment(final Department department);
    Department getDepartmentById(final int id);
    List<Department> getAllDepartments();
    boolean updateDepartment(final int id, final Department department);
    boolean deleteDepartment(final int id);
}
