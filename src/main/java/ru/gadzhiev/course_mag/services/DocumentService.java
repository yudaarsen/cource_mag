package ru.gadzhiev.course_mag.services;


import ru.gadzhiev.course_mag.models.DocumentType;

import java.util.List;

public interface DocumentService {

    DocumentType create(final DocumentType documentType) throws Exception;
    int delete(final DocumentType documentType) throws Exception;
    List<DocumentType> findAll() throws Exception;

}
