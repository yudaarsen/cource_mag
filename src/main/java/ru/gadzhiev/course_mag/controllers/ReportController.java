package ru.gadzhiev.course_mag.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.gadzhiev.course_mag.controllers.catalogs.DeductionController;
import ru.gadzhiev.course_mag.controllers.exceptions.RestApiException;
import ru.gadzhiev.course_mag.models.DocumentType;
import ru.gadzhiev.course_mag.models.reports.BalanceRow;
import ru.gadzhiev.course_mag.models.reports.Osv;
import ru.gadzhiev.course_mag.models.reports.OsvPosition;
import ru.gadzhiev.course_mag.services.ReportService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class ReportController {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @GetMapping(path = "/report/osv")
    @ResponseStatus(code = HttpStatus.OK)
    public Osv getOsv(@RequestParam("from") @DateTimeFormat(pattern = DATE_FORMAT) final Date fromDate,
                      @RequestParam("to") @DateTimeFormat(pattern = DATE_FORMAT) final Date toDate) throws RestApiException {
        try {
            if(fromDate.after(toDate))
                throw new IllegalStateException("Illegal date");
            return reportService.getOsv(fromDate, toDate, DocumentType.TYPE_REVERSE);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new RestApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e) {
            logger.error("Error while getting osv", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting osv");
        }
    }

    @GetMapping(path = "/report/balance")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BalanceRow> getBalance(@RequestParam("from") @DateTimeFormat(pattern = DATE_FORMAT) final Date fromDate,
                                       @RequestParam("to") @DateTimeFormat(pattern = DATE_FORMAT) final Date toDate) throws RestApiException {
        try {
            if(fromDate.after(toDate))
                throw new IllegalStateException("Illegal date");
            return reportService.getBalance(fromDate, toDate);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new RestApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e) {
            logger.error("Error while getting balance", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting balance");
        }
    }
}
