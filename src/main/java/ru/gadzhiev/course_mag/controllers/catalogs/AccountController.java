package ru.gadzhiev.course_mag.controllers.catalogs;


import jakarta.validation.constraints.Digits;
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
import ru.gadzhiev.course_mag.models.Account;
import ru.gadzhiev.course_mag.models.validations.AccountValidation;
import ru.gadzhiev.course_mag.services.AccountService;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/rest",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private AccountService accountService;

    @PostMapping(path = "/account")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Account createAccount(@RequestBody @Validated(value = AccountValidation.class) final Account account) throws RestApiException {
        try {
            Account createdAccount = accountService.create(account);
            logger.debug("Account is created: " + createdAccount.code());
            return createdAccount;
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23505")) {
                    logger.warn("Account with specified code already exists: " + account.code());
                    throw new RestApiException(HttpStatus.CONFLICT, "Account with specified code already exists");
                }
            }
            logger.error("Error while creating account: " + account.code(), e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating account");
        }
    }

    @DeleteMapping(path = "/account/{code}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteAccount(@PathVariable("code") @Digits(integer = 10, fraction = 0) @Size(min = 10, max = 10) final String code) throws RestApiException {
        try {
            accountService.delete(new Account(code, null, null));
            logger.debug("Account is deleted: " + code);
        } catch (IllegalArgumentException e) {
            logger.debug(e.getMessage());
            throw new RestApiException(HttpStatus.NO_CONTENT, "");
        } catch (Exception e) {
            if(e.getCause() instanceof PSQLException) {
                if(((PSQLException)e.getCause()).getSQLState().equals("23503")) {
                    logger.debug("Account has active references: " + code);
                    throw new RestApiException(HttpStatus.BAD_REQUEST, "Account has active references");
                }
            }
            logger.error("Error while deleting account: " + code, e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting account");
        }
    }

    @GetMapping(path = "/accounts")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Account> getAllAccounts() throws RestApiException {
        try {
            return accountService.findAll();
        } catch (Exception e) {
            logger.error("Error while getting accounts: ", e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting accounts");
        }
    }
}
