package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.gadzhiev.course_mag.models.Department;

import java.util.List;

public interface DepartmentDao {

    @SqlQuery("INSERT INTO department VALUES (DEFAULT, :name) RETURNING *")
    @RegisterConstructorMapper(Department.class)
    Department create(@BindMethods final Department department);

    @SqlQuery("UPDATE department SET name = :name WHERE id = :id RETURNING *")
    @RegisterConstructorMapper(Department.class)
    Department update(@BindMethods final Department department);

    @SqlUpdate("DELETE FROM department WHERE id = :id")
    int delete(@BindMethods final Department department);

    @SqlQuery("SELECT * FROM department")
    @RegisterConstructorMapper(Department.class)
    List<Department> findAll();
}
