package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import ru.gadzhiev.course_mag.models.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface AccountDao {

    class AccountRowMapper implements RowMapper<Account> {

        @Override
        public Account map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Account(
                    rs.getString("code"),
                    rs.getString("name"),
                    new Account(
                            rs.getString("parent_id"),
                            null,
                            null
                    )
            );
        }
    }

    @SqlQuery("INSERT INTO account VALUES (:code, :parent?.code, :name) RETURNING *")
    @UseRowMapper(AccountRowMapper.class)
    Account create(@BindMethods final Account account);

    @SqlUpdate("DELETE FROM account WHERE code = :code")
    int delete(@BindMethods final Account account);

    @SqlQuery("SELECT * FROM account")
    @UseRowMapper(AccountRowMapper.class)
    List<Account> getAll();

}
