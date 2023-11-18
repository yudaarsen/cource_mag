package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.Employee;
import ru.gadzhiev.course_mag.models.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao {

    class EmployeeRowMapper implements RowMapper<Employee> {

        @Override
        public Employee map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Employee(
                    rs.getInt("personnel_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("middle_name"),
                    new Function(
                            rs.getInt("function_id"),
                            rs.getString("fname"),
                            new Department(
                                    rs.getInt("department_id"),
                                    rs.getString("dname")
                            )
                    ),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getLong("salary")
            );
        }
    }

    @SqlQuery("INSERT INTO employee VALUES (:personnelNumber, :firstName, :lastName, :middleName, :function.id, :function.department.id, :email, :phone, :salary) " +
            "RETURNING *")
    @RegisterConstructorMapper(Employee.class)
    Employee create(@BindMethods final Employee employee);

    @SqlQuery("UPDATE employee SET first_name = :firstName, last_name = :lastName, middle_name = :middleName, function_id = :function.id," +
            " department_id = :function.department.id, email = :email, phone = :phone, salary = :salary WHERE personnel_number = :personnelNumber" +
            " RETURNING *")
    @RegisterConstructorMapper(Employee.class)
    Employee update(@BindMethods final Employee employee);

    @SqlUpdate("DELETE FROM employee WHERE personnel_number = :personnelNumber")
    int delete(@BindMethods final Employee employee);

    @SqlQuery("SELECT employee.*, function.name AS fname, department.name AS dname" +
            " FROM employee" +
            " INNER JOIN function ON employee.function_id = function.id" +
            " INNER JOIN department ON function.department_id = department.id")
    @UseRowMapper(EmployeeRowMapper.class)
    List<Employee> getAllEmployees();

    @SqlQuery("SELECT employee.*, function.name AS fname, department.name AS dname" +
            " FROM employee" +
            " INNER JOIN function ON employee.function_id = function.id" +
            " INNER JOIN department ON function.department_id = department.id" +
            " WHERE employee.personnel_number = :personnelNumber")
    @UseRowMapper(EmployeeRowMapper.class)
    Employee getById(@BindMethods final Employee employee);
}
