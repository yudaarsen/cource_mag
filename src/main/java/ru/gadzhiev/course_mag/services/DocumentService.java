package ru.gadzhiev.course_mag.services;


import ru.gadzhiev.course_mag.models.Document;
import ru.gadzhiev.course_mag.models.DocumentPosition;
import ru.gadzhiev.course_mag.models.DocumentType;
import ru.gadzhiev.course_mag.models.Employee;

import java.util.List;

public interface DocumentService {

    DocumentType createDocumentType(final DocumentType documentType) throws Exception;
    int deleteDocumentType(final DocumentType documentType) throws Exception;
    List<DocumentType> findAllDocumentTypes() throws Exception;
    Document createDocument(final Document document) throws Exception;
    Document findDocumentById(final Document document) throws Exception;
    List<Document> getDocuments() throws Exception;
    Document reverseDocument(final Document document) throws Exception;
    List<DocumentPosition> calculatePayment(final Employee employee) throws Exception;
}
