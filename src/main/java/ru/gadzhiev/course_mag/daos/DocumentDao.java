package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.*;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.*;
import ru.gadzhiev.course_mag.Utils;
import ru.gadzhiev.course_mag.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
WITH RECURSIVE consts (date_from, date_to) AS (VALUES (date '2023-11-01', date '2023-11-30'))
, osv AS (
	SELECT dp.account_id
		, SUM(CASE WHEN dp.pos_type = 'D' THEN dp.amount ELSE 0 END) AS debit
		, SUM(CASE WHEN dp.pos_type = 'C' THEN dp.amount ELSE 0 END) AS credit
	FROM document_position AS dp
		INNER JOIN document AS d
			ON dp.document_id = d.id
		CROSS JOIN consts
	WHERE d.reverse_document IS NULL
		AND d.type_id != 'REVE'
		AND d.posting_date BETWEEN date_from AND date_to
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
)
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

 */

public interface DocumentDao {

    class DocumentRowReducer implements LinkedHashMapRowReducer<Integer, Document> {
        @Override
        public void accumulate(Map<Integer, Document> container, RowView rowView) {
            Document document = container.computeIfAbsent(rowView.getColumn("d_id", Integer.class),
                    id -> {
                        DocumentType documentType = rowView.getRow(DocumentType.class);
                        return new Document(
                                id,
                                documentType,
                                Utils.parseDate(rowView.getColumn("d_posting_date", String.class)),
                                rowView.getColumn("d_note", String.class),
                                new ArrayList<>(),
                                rowView.getColumn("d_reverse_document", Integer.class)
                        );
                    });

            document.documentPositions().add(new DocumentPosition(
                    document.id(),
                    rowView.getColumn("ds_pos_num", Integer.class),
                    rowView.getColumn("ds_pos_type", Character.class),
                    rowView.getRow(Account.class),
                    rowView.getColumn("ds_amount", Double.class),
                    rowView.getRow(Employee.class).personnelNumber() > 0 ? rowView.getRow(Employee.class) : null,
                    rowView.getColumn("ds_note", String.class)
            ));
        }
    }

    class EmployeeNameRowMapper implements RowMapper<Employee> {

        @Override
        public Employee map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Employee(
                    rs.getInt("em_personnel_number"),
                    rs.getString("em_first_name"),
                    rs.getString("em_last_name"),
                    rs.getString("em_middle_name"),
                    null,
                    null,
                    null,
                    0
            );
        }
    }

    class DocumentRowMapper implements RowMapper<Document> {
        @Override
        public Document map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Document(
                    rs.getInt("id"),
                    new DocumentType(
                            rs.getString("type_id"),
                            null
                    ),
                    rs.getDate("posting_date"),
                    rs.getString("note"),
                    null,
                    null
            );
        }
    }

    @SqlQuery("INSERT INTO document VALUES (:id, UPPER(:documentType.code), :postingDate, :note, :reverseDocument) RETURNING *")
    @UseRowMapper(DocumentRowMapper.class)
    Document create(@BindMethods final Document document);

    @SqlBatch("INSERT INTO document_position VALUES (:pos.documentId, :pos.posNum, :pos.posType, :pos.account.code, :pos.amount, " +
            ":pos.employee?.personnelNumber, :pos.note)")
    int[] createPositions(@BindMethods(value = "pos") final List<DocumentPosition> documentPositions);

    @SqlQuery("""
            SELECT d.id AS "d_id"
            , d.posting_date AS "d_posting_date"
            , d.note AS "d_note"
            , d.reverse_document AS "d_reverse_document"
            , t.code AS "t_code"
            , t.name AS "t_name"
            , ds.pos_num AS "ds_pos_num"
            , ds.pos_type AS "ds_pos_type"
            , ds.amount AS "ds_amount"
            , ds.note AS "ds_note"
            , ac.code AS "ac_code"
            , ac.name AS "ac_name"
            , em.personnel_number AS "em_personnel_number"
            , em.first_name AS "em_first_name"
            , em.last_name AS "em_last_name"
            , em.middle_name AS "em_middle_name"
            FROM document AS d
            	LEFT JOIN document_position AS ds
            		ON d.id = ds.document_id
            	LEFT JOIN document_type AS t
            		ON d.type_id = t.code
            	LEFT JOIN account AS ac
            		ON ds.account_id = ac.code
            	LEFT JOIN employee AS em
            		ON ds.personnel_number = em.personnel_number
            WHERE d.id = :id;
            """)
    @RegisterConstructorMapper(value = DocumentType.class, prefix = "t")
    @RegisterConstructorMapper(value = Account.class, prefix = "ac")
    @RegisterRowMapper(value = EmployeeNameRowMapper.class)
    @UseRowReducer(DocumentRowReducer.class)
    Document getById(@BindMethods final Document document);

    @SqlQuery("SELECT nextval(pg_get_serial_sequence('document', 'id'))")
    int getNextDocumentId();

    @SqlQuery("SELECT * FROM document")
    @UseRowMapper(DocumentRowMapper.class)
    List<Document> getDocuments();

    @SqlUpdate("UPDATE document SET reverse_document = :reverseDocument.id WHERE id = :document.id")
    int reverseDocument(@BindMethods(value = "document") final Document document, @BindMethods(value = "reverseDocument") final Document reverseDocument);
}
