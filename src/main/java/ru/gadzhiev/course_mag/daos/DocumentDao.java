package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import ru.gadzhiev.course_mag.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DocumentDao {

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
                    null
            );
        }
    }

    class DocumentPositionMapper implements RowMapper<DocumentPosition> {

        @Override
        public DocumentPosition map(ResultSet rs, StatementContext ctx) throws SQLException {
            Employee employee = new Employee(
                    rs.getInt("personnel_number"),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0
            );
            if(employee.personnelNumber() == 0)
                employee = null;
            return new DocumentPosition(
                    rs.getInt("document_id"),
                    rs.getInt("pos_num"),
                    rs.getString("pos_type").charAt(0),
                    new Account(
                            rs.getString("account_id"),
                            null,
                            null
                    ),
                    rs.getLong("amount"),
                    employee,
                    rs.getString("note")
            );
        }
    }

    @SqlQuery("INSERT INTO document VALUES (:id, UPPER(:documentType.code), :postingDate, :note) RETURNING *")
    @UseRowMapper(DocumentRowMapper.class)
    Document create(@BindMethods final Document document);

    @SqlBatch("INSERT INTO document_position VALUES (:pos.documentId, :pos.posNum, :pos.posType, :pos.account.code, :pos.amount, " +
            ":pos.employee?.personnelNumber, :pos.note)")
    int[] createPositions(@BindMethods(value = "pos") final List<DocumentPosition> documentPositions);

    @SqlQuery("SELECT * FROM document WHERE id = :id")
    @UseRowMapper(DocumentRowMapper.class)
    Document getById(@BindMethods final Document document);

    @SqlQuery("SELECT * FROM document_position WHERE document_id = :id")
    @UseRowMapper(DocumentPositionMapper.class)
    List<DocumentPosition> getDocumentPositions(@BindMethods final Document document);

    @SqlQuery("SELECT nextval(pg_get_serial_sequence('document', 'id'))")
    int getNextDocumentId();
}
