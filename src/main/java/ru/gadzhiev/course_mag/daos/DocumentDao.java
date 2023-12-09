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

    class DocumentHeaderRowMapper implements RowMapper<Document> {
        @Override
        public Document map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Document(
                    rs.getInt("id"),
                    new DocumentType(
                            rs.getString("type_id"),
                            rs.getString("type_name")
                    ),
                    rs.getDate("posting_date"),
                    rs.getString("note"),
                    null,
                    rs.getInt("reverse_document")
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

    @SqlQuery("""
        SELECT document.*
            , type.name AS "type_name"
        FROM document
            LEFT JOIN document_type AS type
                ON document.type_id = type.code
    """)
    @UseRowMapper(DocumentHeaderRowMapper.class)
    List<Document> getDocuments();

    @SqlUpdate("UPDATE document SET reverse_document = :reverseDocument.id WHERE id = :document.id")
    int reverseDocument(@BindMethods(value = "document") final Document document, @BindMethods(value = "reverseDocument") final Document reverseDocument);
}
