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
        if(account.code().equals(Account.ACCOUNT_00)
            || account.code().equals(Account.ACCOUNT_20)
            || account.code().equals(Account.ACCOUNT_26)
            || account.code().equals(Account.ACCOUNT_51)
            || account.code().equals(Account.ACCOUNT_68)
            || account.code().equals(Account.ACCOUNT_70)
            || account.code().equals(Account.ACCOUNT_69)
        )
            throw new IllegalArgumentException("Account " + account.code() + " cannot be deleted!");
        return jdbi.withExtension(AccountDao.class, extension -> extension.delete(account));
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
