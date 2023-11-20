package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import ru.gadzhiev.course_mag.models.Account;
import ru.gadzhiev.course_mag.models.Deduction;
import ru.gadzhiev.course_mag.models.Employee;
import ru.gadzhiev.course_mag.models.EmployeeDeduction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DeductionDao {
    class DeductionRowMapper implements RowMapper<Deduction> {
        @Override
        public Deduction map(ResultSet rs, StatementContext ctx) throws SQLException {
           return new Deduction(
                   rs.getString("code"),
                   new Account(
                           rs.getString("account_id"),
                           null,
                           null
                   ),
                   rs.getInt("rate")
           );
        }
    }

    @SqlQuery("INSERT INTO deduction VALUES (UPPER(:code), :account.code, :rate) RETURNING *")
    @UseRowMapper(DeductionRowMapper.class)
    Deduction create(@BindMethods final Deduction deduction);

    @SqlUpdate("DELETE FROM deduction WHERE code = :code")
    int delete(@BindMethods final Deduction deduction);
    @SqlQuery("SELECT * FROM deduction")
    @UseRowMapper(DeductionRowMapper.class)
    List<Deduction> getAll();

    @SqlQuery("SELECT d.code, d.account_id, GREATEST(d.rate, ed.rate) AS rate FROM employee_deduction AS ed INNER JOIN deduction AS d ON ed.deduction_id = d.code " +
            "WHERE ed.personnel_number = :employee.personnelNumber AND ed.deduction_id = UPPER(:deduction.code)")
    @UseRowMapper(DeductionRowMapper.class)
    Deduction getEmployeeDeduction(@BindMethods final EmployeeDeduction employeeDeduction);

    @SqlUpdate("INSERT INTO employee_deduction VALUES (:employee.personnelNumber, UPPER(:deduction.code), :deduction.rate)")
    int createEmployeeDeduction(@BindMethods final EmployeeDeduction employeeDeduction);

    @SqlUpdate("DELETE FROM employee_deduction WHERE personnel_number = :employee.personnelNumber AND deduction_id = UPPER(:deduction.code)")
    int deleteEmployeeDeduction(@BindMethods final EmployeeDeduction employeeDeduction);

    @SqlQuery("SELECT d.code, d.account_id, GREATEST(d.rate, ed.rate) AS rate FROM employee_deduction AS ed INNER JOIN deduction AS d ON ed.deduction_id = d.code " +
            "WHERE ed.personnel_number = :personnelNumber")
    @UseRowMapper(DeductionRowMapper.class)
    List<Deduction> getEmployeeDeductions(@BindMethods final Employee employee);
}
