package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.AccountDao;
import ru.gadzhiev.course_mag.models.Account;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private Jdbi jdbi;

    @Override
    public Account create(Account account) throws Exception {
        return jdbi.withExtension(AccountDao.class, extension -> extension.create(account));
    }

    @Override
    public int delete(Account account) throws Exception {
        int result = jdbi.withExtension(AccountDao.class, extension -> extension.delete(account));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Account does not exist: " + account.code());
    }

    @Override
    public List<Account> findAll() throws Exception {
        return jdbi.withExtension(AccountDao.class, AccountDao::getAll);
    }

    @Override
    public Account findById(Account account) throws Exception {
        return jdbi.withExtension(AccountDao.class, extension -> extension.findById(account));
    }
}
