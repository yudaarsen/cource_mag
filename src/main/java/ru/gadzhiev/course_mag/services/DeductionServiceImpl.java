package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.DeductionDao;
import ru.gadzhiev.course_mag.models.Account;
import ru.gadzhiev.course_mag.models.Deduction;

import java.util.List;

@Service
public class DeductionServiceImpl implements DeductionService {

    @Autowired
    private Jdbi jdbi;

    @Autowired
    private AccountService accountService;

    @Override
    public Deduction create(Deduction deduction) throws Exception {
        Account account = accountService.findById(new Account(deduction.account().code(), null, null));
        if(account == null)
            throw new IllegalArgumentException("Account does not exist: " + deduction.account().code());
        return jdbi.withExtension(DeductionDao.class, extension -> extension.create(deduction));
    }

    @Override
    public int delete(Deduction deduction) throws Exception {
        int result = jdbi.withExtension(DeductionDao.class, extension -> extension.delete(deduction));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Deduction does not exist");
    }

    @Override
    public List<Deduction> findAll() throws Exception {
        return jdbi.withExtension(DeductionDao.class, DeductionDao::getAll);
    }
}
