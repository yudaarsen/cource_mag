package ru.gadzhiev.course_mag.daos;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.gadzhiev.course_mag.models.DocumentType;

import java.util.List;

public interface DocumentTypeDao {

    @SqlQuery("INSERT INTO document_type VALUES (UPPER(:code), :name) RETURNING *")
    @RegisterConstructorMapper(DocumentType.class)
    DocumentType create(@BindMethods final DocumentType documentType);

    @SqlUpdate("DELETE FROM document_type WHERE code = :code")
    int delete(@BindMethods final DocumentType documentType);

    @SqlQuery("SELECT * FROM document_type")
    @RegisterConstructorMapper(DocumentType.class)
    List<DocumentType> getAll();
}
