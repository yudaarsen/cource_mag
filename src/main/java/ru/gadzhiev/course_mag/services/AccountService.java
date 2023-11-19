package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.Account;

import java.util.List;

public interface AccountService {

    Account create(final Account account) throws Exception;

    int delete(final Account account) throws Exception;

    List<Account> findAll() throws Exception;

    Account findById(final Account account) throws Exception;
}
