package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import ru.gadzhiev.course_mag.models.Account;
import ru.gadzhiev.course_mag.models.reports.OsvPosition;
import ru.gadzhiev.course_mag.models.reports.OsvTotals;
import ru.gadzhiev.course_mag.models.reports.VedRow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ReportDao {

    class OsvPositionRowMapper implements RowMapper<OsvPosition> {

        @Override
        public OsvPosition map(ResultSet rs, StatementContext ctx) throws SQLException {
            String parentCode = rs.getString("parent_id");
            Account parent = parentCode == null ? null : new Account(parentCode, null, null);
            return new OsvPosition(
                    new Account(
                            rs.getString("account_id"),
                            rs.getString("name"),
                            parent
                    ),
                    0,
                    0,
                    rs.getDouble("debit"),
                    rs.getDouble("credit"),
                    0,
                    0
            );
        }
    }

    @SqlQuery("""
            WITH RECURSIVE osv AS (
              SELECT dp.account_id
                , SUM(CASE WHEN dp.pos_type = 'D' THEN dp.amount ELSE 0 END) AS debit
                , SUM(CASE WHEN dp.pos_type = 'C' THEN dp.amount ELSE 0 END) AS credit
              FROM document_position AS dp
                INNER JOIN document AS d
                    ON dp.document_id = d.id
              WHERE d.reverse_document IS NULL
                AND d.type_id != :reverseType
                AND d.posting_date BETWEEN :fromDate AND :toDate
              GROUP BY account_id
              ), rec_osv AS (
              SELECT parent_id AS account_id
                , SUM(debit) AS debit
                , SUM(credit) AS credit
              FROM osv
                INNER JOIN account
                    ON osv.account_id = account.code
              WHERE parent_id IS NOT NULL
              GROUP BY parent_id
                         
              UNION ALL
                         
              SELECT parent_id AS account_id
                , SUM(debit) OVER (PARTITION BY parent_id) AS debit
                , SUM(credit) OVER (PARTITION BY parent_id) AS credit
              FROM rec_osv
                INNER JOIN account
                    ON rec_osv.account_id = account.code
              WHERE parent_id IS NOT NULL
              ), res_osv AS (
                SELECT account_id
                , account.name
                , debit
                , credit
                FROM (
                SELECT account_id
                    , SUM(debit) AS debit
                    , SUM(credit) AS credit
                FROM (
                    SELECT *
                    FROM osv
                         
                    UNION ALL
                         
                    SELECT *
                    FROM rec_osv
                         
                    UNION ALL
                         
                    SELECT code AS account_id
                    , 0
                    , 0
                    FROM account
                    ORDER BY account_id
                ) AS totals
                GROUP BY account_id
                ) AS grouped_totals
                INNER JOIN account
                ON account.code = grouped_totals.account_id
              )
              SELECT res_osv.*
            , account.parent_id AS "parent_id"
            FROM res_osv
                LEFT JOIN account
                    ON res_osv.account_id = account.code;
            """)
    @UseRowMapper(OsvPositionRowMapper.class)
    List<OsvPosition> getOsv(@Bind final Date fromDate, @Bind final Date toDate, @Bind final String reverseType);

    @SqlQuery("""
            WITH RECURSIVE osv AS (
            SELECT dp.account_id
            	, SUM(CASE WHEN dp.pos_type = 'D' THEN dp.amount ELSE 0 END) AS debit
            	, SUM(CASE WHEN dp.pos_type = 'C' THEN dp.amount ELSE 0 END) AS credit
            FROM document_position AS dp
            	INNER JOIN document AS d
            		ON dp.document_id = d.id
            WHERE d.reverse_document IS NULL
            	AND d.type_id != :reverseType
            	AND d.posting_date BETWEEN :fromDate AND :toDate
            GROUP BY account_id
            ), rec_osv AS (
            SELECT parent_id AS account_id
            	, SUM(debit) AS debit
            	, SUM(credit) AS credit
            FROM osv
            	INNER JOIN account
            		ON osv.account_id = account.code
            WHERE parent_id IS NOT NULL
            GROUP BY parent_id
                        
            UNION ALL
                        
            SELECT parent_id AS account_id
            	, SUM(debit) OVER (PARTITION BY parent_id) AS debit
            	, SUM(credit) OVER (PARTITION BY parent_id) AS credit
            FROM rec_osv
            	INNER JOIN account
            		ON rec_osv.account_id = account.code
            WHERE parent_id IS NOT NULL
            ), res_osv AS (
            	SELECT account_id
            	, account.name
            	, debit
            	, credit
            	FROM (
            	SELECT account_id
            		, SUM(debit) AS debit
            		, SUM(credit) AS credit
            	FROM (
            		SELECT *
            		FROM osv
                        
            		UNION ALL
                        
            		SELECT *
            		FROM rec_osv
                        
            		UNION ALL
                        
            		SELECT code AS account_id
            		, 0
            		, 0
            		FROM account
            		ORDER BY account_id
            	) AS totals
            	GROUP BY account_id
            	) AS grouped_totals
            	INNER JOIN account
            	ON account.code = grouped_totals.account_id
            )
            SELECT SUM(debit) AS debit
            	, SUM(credit) AS credit
            FROM res_osv
            	LEFT JOIN account AS acc
            		ON account_id = acc.code
            WHERE acc.parent_id IS NULL;
            """)
    @RegisterConstructorMapper(OsvTotals.class)
    OsvTotals getOsvTotals(@Bind final Date fromDate, @Bind final Date toDate, @Bind final String reverseType);

    @SqlQuery(
        """
        WITH ved AS (
            SELECT employee.personnel_number
                , COUNT(employee.personnel_number) AS present
            FROM employee
                INNER JOIN timesheet
                    ON employee.personnel_number = timesheet.personnel_number
            WHERE timesheet.present = true
                AND date (timesheet.year || '-' || timesheet.month || '-' || timesheet.day) >= :fromDate
                AND date (timesheet.year || '-' || timesheet.month || '-' || timesheet.day) <= :toDate
            GROUP BY employee.personnel_number
        )
        SELECT ved.personnel_number
            , employee.last_name || ' ' || LEFT(employee.first_name, 1) || '.' || LEFT(employee.middle_name, 1) || '.' AS fio
            , function.name AS fname
            , employee.salary
            , present
        FROM ved
            INNER JOIN employee
                ON employee.personnel_number = ved.personnel_number
            INNER JOIN function
                ON employee.function_id = function.id;
        """
    )
    @RegisterConstructorMapper(VedRow.class)
    List<VedRow> getVed(@Bind final Date fromDate, @Bind final Date toDate);
}
