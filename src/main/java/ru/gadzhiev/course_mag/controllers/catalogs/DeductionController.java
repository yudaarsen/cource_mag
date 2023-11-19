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
import ru.gadzhiev.course_mag.models.Deduction;
import ru.gadzhiev.course_mag.models.validations.DeductionValidation;
import ru.gadzhiev.course_mag.services.DeductionService;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class DeductionController {

    private final Logger logger = LoggerFactory.getLogger(DeductionController.class);

    @Autowired
    private DeductionService deductionService;

    @PostMapping(path = "/deduction")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Deduction createDeduction(@RequestBody @Validated(DeductionValidation.class) final Deduction deduction) throws RestApiException {
       try {
          Deduction createdDeduction = deductionService.create(deduction);
          logger.debug("Deduction is created: " + createdDeduction.code());
          return createdDeduction;
       } catch (IllegalArgumentException e) {
           logger.warn(e.getMessage());
           throw new RestApiException(HttpStatus.BAD_REQUEST, e.getMessage());
       } catch (Exception e) {
           if(e.getCause() instanceof PSQLException) {
               if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                   logger.warn("Deduction code already exists: " + deduction.code());
                   throw new RestApiException(HttpStatus.CONFLICT, "Deduction code already exists");
               }
           }
           logger.error("Error while creating deduction", e);
           throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating deduction");
       }
    }

    @DeleteMapping(path = "/deduction/{code}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteDeduction(@PathVariable("code") @NotNull @NotBlank @Size(min = 4, max = 4) final String code) throws RestApiException {
        try {
            deductionService.delete(new Deduction(code.toUpperCase(), null, 0));
            logger.debug("Deduction is deleted: " + code);
        } catch (IllegalArgumentException e) {
            logger.debug(e.getMessage());
            throw new RestApiException(HttpStatus.NO_CONTENT, "");
        } catch (Exception e) {
            logger.error("Error while deleting deduction", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting deduction");
        }
    }

    @GetMapping(path = "/deductions")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Deduction> getAllDeductions() throws RestApiException {
        try {
            return deductionService.findAll();
        } catch (Exception e) {
            logger.error("Error while getting deduction", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting deduction");
        }
    }
}
