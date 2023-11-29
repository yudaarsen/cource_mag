package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.transaction.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.DocumentDao;
import ru.gadzhiev.course_mag.daos.DocumentTypeDao;
import ru.gadzhiev.course_mag.models.Document;
import ru.gadzhiev.course_mag.models.DocumentPosition;
import ru.gadzhiev.course_mag.models.DocumentType;
import ru.gadzhiev.course_mag.models.Employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private Jdbi jdbi;

    @Override
    public DocumentType createDocumentType(DocumentType documentType) throws Exception {
        return jdbi.withExtension(DocumentTypeDao.class, extension -> extension.create(documentType));
    }

    @Override
    public int deleteDocumentType(DocumentType documentType) throws Exception {
        int result = jdbi.withExtension(DocumentTypeDao.class, extension -> extension.delete(documentType));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Document type does not exist");
    }

    @Override
    public List<DocumentType> findAllDocumentTypes() throws Exception {
        return jdbi.withExtension(DocumentTypeDao.class, DocumentTypeDao::getAll);
    }

    @Override
    public Document createDocument(final Document document) throws Exception {
        if(!validatePositionType(document))
            throw new IllegalArgumentException("Illegal position type");
        if(!validateDocumentBalance(document))
            throw new IllegalArgumentException("Balance of document must be 0");
        if(document.documentType().code().equals(DocumentType.TYPE_REVERSE))
            throw new IllegalArgumentException("Document with this type cannot be created");
        Document createdDocument = jdbi.inTransaction(handle -> {
            int id = handle.attach(DocumentDao.class).getNextDocumentId();
            Document preparedDocument = prepareDocument(new Document (
                id,
                document.documentType(),
                document.postingDate(),
                document.note(),
                document.documentPositions(),
                0
            ));
            Document transactionalDocument = handle.attach(DocumentDao.class).create(preparedDocument);
            handle.attach(DocumentDao.class).createPositions(preparedDocument.documentPositions());
            return transactionalDocument;
        });
        return findDocumentById(createdDocument);
    }

    @Override
    public Document findDocumentById(final Document document) throws Exception {
        return jdbi.withExtension(DocumentDao.class, extension -> extension.getById(document));
    }

    @Override
    public List<Document> getDocuments() throws Exception {
        return jdbi.withExtension(DocumentDao.class, DocumentDao::getDocuments);
    }

    @Override
    public Document reverseDocument(final Document document) throws Exception {
        Document documentToReverse = findDocumentById(document);
        if(documentToReverse == null)
            throw new IllegalArgumentException("Document does not exist: " + document.id());
        if(documentToReverse.documentType().code().equals(DocumentType.TYPE_REVERSE))
            throw new IllegalArgumentException("Document with this type cannot be reversed: " + document.id());
        if(documentToReverse.reverseDocument() > 0)
            throw new IllegalArgumentException("Document is already reversed: " + document.id());
        if(documentToReverse.postingDate().after(document.postingDate()))
            throw new IllegalArgumentException("Illegal posting date: " + document.id());

        Document reverseDocument = jdbi.inTransaction(handle -> {
            int id = handle.attach(DocumentDao.class).getNextDocumentId();
            Document preparedDocument = new Document(
                    id,
                    new DocumentType(
                            DocumentType.TYPE_REVERSE,
                            null
                    ),
                    document.postingDate(),
                    "Документ сторно от " + new SimpleDateFormat("dd.MM.yyyy").format(document.postingDate()) +  " к документу №" + documentToReverse.id(),
                    copyPositions(id, documentToReverse.documentPositions()),
                    documentToReverse.id()
            );
            handle.attach(DocumentDao.class).create(preparedDocument);
            handle.attach(DocumentDao.class).createPositions(preparedDocument.documentPositions());
            handle.attach(DocumentDao.class).reverseDocument(documentToReverse,
                    new Document(id, null, null, null, null, 0)
            );
            return handle.attach(DocumentDao.class).getById(preparedDocument);
        });
        return reverseDocument;
    }

    private Document prepareDocument(final Document document) {
        List<DocumentPosition> preparedPositions = new ArrayList<>();
        int counter = 1;
        for(DocumentPosition position : document.documentPositions()) {
            preparedPositions.add(new DocumentPosition(
                    document.id(),
                    counter,
                    position.posType(),
                    position.account(),
                    position.amount(),
                    position.employee(),
                    position.note()
            ));
            counter++;
        }
        return new Document(
                document.id(),
                document.documentType(),
                document.postingDate(),
                document.note(),
                preparedPositions,
                document.reverseDocument()
        );
    }

    private boolean validatePositionType(final Document document) {
        for(DocumentPosition documentPosition : document.documentPositions()) {
            if(documentPosition.posType() != DocumentPosition.TYPE_CREDIT
                    && documentPosition.posType() != DocumentPosition.TYPE_DEBIT)
                return false;
        }
        return true;
    }

    private boolean validateDocumentBalance(final Document document) {
        long balance = 0;
        for (DocumentPosition documentPosition : document.documentPositions()) {
            balance = documentPosition.posType() == DocumentPosition.TYPE_DEBIT ?
                    balance + documentPosition.amount()
                    : balance - documentPosition.amount();
        }
        return balance == 0;
    }

    private List<DocumentPosition> copyPositions(final int documentId, final List<DocumentPosition> positions) {
        List<DocumentPosition> copiedPositions = new ArrayList<>();
        for(DocumentPosition documentPosition : positions) {
            copiedPositions.add(new DocumentPosition(
                    documentId,
                    documentPosition.posNum(),
                    documentPosition.posType(),
                    documentPosition.account(),
                    documentPosition.amount(),
                    documentPosition.employee(),
                    documentPosition.note()
            ));
        }
        return copiedPositions;
    }
}
