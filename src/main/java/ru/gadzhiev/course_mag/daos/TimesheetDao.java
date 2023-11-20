package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import ru.gadzhiev.course_mag.models.Employee;
import ru.gadzhiev.course_mag.models.Timesheet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TimesheetDao {

    class TimesheetRowMapper implements RowMapper<Timesheet> {

        @Override
        public Timesheet map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Timesheet(
                    new Employee(
                            rs.getInt("personnel_number"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            0
                    ),
                    rs.getInt("year"),
                    rs.getInt("month"),
                    rs.getInt("day"),
                    rs.getBoolean("present")
            );
        }
    }

    @SqlQuery("INSERT INTO timesheet VALUES (:employee.personnelNumber, :year, :month, :day, :present) " +
            "ON CONFLICT (personnel_number, year, month, day) DO UPDATE SET present = :present RETURNING *")
    @UseRowMapper(TimesheetRowMapper.class)
    Timesheet create(@BindMethods final Timesheet timesheet);

    @SqlUpdate("DELETE FROM timesheet WHERE personnel_number = :employee.personnelNumber AND year = :year AND month = :month AND day = :day")
    int delete(@BindMethods final Timesheet timesheet);

    @SqlQuery("SELECT * FROM timesheet WHERE personnel_number = :personnelNumber AND year = :year AND month = :month")
    @UseRowMapper(TimesheetRowMapper.class)
    List<Timesheet> getEmployeeTimesheet(@BindMethods final Employee employee, @Bind final int year, @Bind final int month);
}
