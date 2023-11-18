package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.DocumentTypeDao;
import ru.gadzhiev.course_mag.models.DocumentType;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private Jdbi jdbi;

    @Override
    public DocumentType create(DocumentType documentType) throws Exception {
        return jdbi.withExtension(DocumentTypeDao.class, extension -> extension.create(documentType));
    }

    @Override
    public int delete(DocumentType documentType) throws Exception {
        int result = jdbi.withExtension(DocumentTypeDao.class, extension -> extension.delete(documentType));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Document type does not exist");
    }

    @Override
    public List<DocumentType> findAll() throws Exception {
        return jdbi.withExtension(DocumentTypeDao.class, DocumentTypeDao::getAll);
    }
}
