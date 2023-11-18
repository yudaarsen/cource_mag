package ru.gadzhiev.course_mag.daos;


import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface FunctionDao {

    class FunctionRowMapper implements RowMapper<Function> {
        @Override
        public Function map(ResultSet rs, StatementContext ctx) throws SQLException {
            Department department = new Department(rs.getInt("did"), rs.getString("dname"));
            return new Function(rs.getInt("fid"), rs.getString("fname"), department);
        }
    }

    @SqlQuery("INSERT INTO function VALUES (:department.id, DEFAULT, :name) RETURNING *")
    @RegisterConstructorMapper(Function.class)
    Function create(@BindMethods final Function function) throws Exception;

    @SqlQuery("SELECT function.id AS fid, function.name AS fname, department.id AS did, department.name AS dname " +
            "FROM function INNER JOIN department ON function.department_id = department.id WHERE function.id = :id")
    @UseRowMapper(FunctionRowMapper.class)
    Function findById(@BindMethods final Function function) throws Exception;

    @SqlQuery("UPDATE function SET name = :name WHERE id = :id RETURNING *")
    @RegisterConstructorMapper(Function.class)
    Function update(@BindMethods final Function function) throws Exception;

    @SqlUpdate("DELETE FROM function WHERE id = :id")
    int delete(@BindMethods final Function function) throws Exception;

    @SqlQuery("SELECT function.id, function.name FROM function INNER JOIN department ON function.department_id = department.id " +
            "WHERE department.id = :id")
    @RegisterConstructorMapper(Function.class)
    List<Function> getFunctionsForDepartment(@BindMethods final Department department) throws Exception;
}
