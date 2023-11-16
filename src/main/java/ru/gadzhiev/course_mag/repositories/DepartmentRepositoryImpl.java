package ru.gadzhiev.course_mag.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.gadzhiev.course_mag.models.Department;

import java.sql.Types;
import java.util.List;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final static String SQL_CREATE_DEPARTMENT = "INSERT INTO department VALUES (DEFAULT, ?)";
    private final static String SQL_SELECT_DEPARTMENTS = "SELECT * FROM department";
    private final static String SQL_SELECT_DEPARTMENT_BY_ID = "SELECT * FROM department WHERE id = ?";
    private final static String SQL_UPDATE_DEPARTMENT = "UPDATE department SET name = ? WHERE id = ?";
    private final static String SQL_DELETE_DEPARTMENT = "DELETE FROM department WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean createDepartment(Department department) {
        try {
            return jdbcTemplate.update(SQL_CREATE_DEPARTMENT, new Object[]{department.name()}, new int[]{Types.VARCHAR}) > 0;
        } catch (Exception e) {
            return false;
        }
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
        try {
            return jdbcTemplate.update(SQL_DELETE_DEPARTMENT, new Object[]{ id }, new int[]{ Types.INTEGER }) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
