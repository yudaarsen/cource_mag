package ru.gadzhiev.course_mag.controllers.catalogs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gadzhiev.course_mag.controllers.exceptions.RestApiException;
import ru.gadzhiev.course_mag.models.Document;
import ru.gadzhiev.course_mag.models.DocumentType;
import ru.gadzhiev.course_mag.models.validations.DocumentTypeValidation;
import ru.gadzhiev.course_mag.models.validations.DocumentValidation;
import ru.gadzhiev.course_mag.services.DocumentService;

import java.sql.BatchUpdateException;
import java.util.List;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class DocumentController {

    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;

    @PostMapping(path = "/document_type")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DocumentType createDocumentType (
            @RequestBody @Validated(value = DocumentTypeValidation.class) final DocumentType documentType) throws RestApiException {
        try {
            DocumentType createdDocumentType = documentService.createDocumentType(documentType);
            logger.debug("Document type is created: " + createdDocumentType.code());
            return createdDocumentType;
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if (((PSQLException) e.getCause()).getSQLState().equals("23505")) {
                    logger.warn("Document type with specified code already exists: " + documentType.code());
                    throw new RestApiException(HttpStatus.CONFLICT, "Document type with specified code already exists");
                }
            }
            logger.error("Error while creating document type", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating document type");
        }
    }

    @DeleteMapping(path = "/document_type/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteDocumentType(@PathVariable("id") @Size(min = 4, max = 4) @NotBlank @NotNull final String code) throws RestApiException {
        try {
            documentService.deleteDocumentType(new DocumentType(code.toUpperCase(), null));
            logger.debug("Document type is deleted: " + code);
        } catch (IllegalArgumentException e) {
            logger.debug(e.getMessage());
            throw new RestApiException(HttpStatus.NO_CONTENT, "");
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23503")) {
                    logger.warn("Document type has references", e);
                    throw new RestApiException(HttpStatus.BAD_REQUEST, "Document type has references");
                }
            }
            logger.error("Error while deleting document type", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting document type");
        }
    }

    @GetMapping(path = "/document_types")
    public List<DocumentType> getAll() throws RestApiException {
        try {
            return documentService.findAllDocumentTypes();
        } catch (Exception e) {
            logger.error("Error while getting document types", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting document types");
        }
    }

    @PostMapping(path = "/document")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Document createDocument(@RequestBody @Validated(value = DocumentValidation.class) final Document document) throws RestApiException {
        try {
            Document createdDocument = documentService.createDocument(document);
            logger.debug("Document is created: " + createdDocument.id());
            return createdDocument;
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            throw new RestApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            if(e.getCause() instanceof BatchUpdateException) {
                if(((BatchUpdateException)e.getCause()).getSQLState().equals("23503")) {
                    logger.warn(e.getMessage());
                    throw new RestApiException(HttpStatus.BAD_REQUEST, "Illegal arguments");
                }
            }
            logger.error("Error while creating document", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating document");
        }
    }
}
