package ru.gadzhiev.course_mag.repositories;

import org.springframework.stereotype.Repository;
import ru.gadzhiev.course_mag.models.Department;

import java.util.List;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {
    @Override
    public boolean createDepartment(Department department) {
        return false;
    }

    @Override
    public Department getDepartmentById(int id) {
        return null;
    }

    @Override
    public List<Department> getAllDepartments() {
        return null;
    }

    @Override
    public boolean updateDepartment(int id, Department department) {
        return false;
    }

    @Override
    public boolean deleteDepartment(int id) {
        return false;
    }
}
