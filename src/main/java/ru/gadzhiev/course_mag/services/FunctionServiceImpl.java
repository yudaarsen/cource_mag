package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.FunctionDao;
import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.Function;

import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private Jdbi jdbi;

    @Override
    public Function create(Function function) throws Exception {
        Function createdFunction = jdbi.withExtension(FunctionDao.class, extension -> extension.create(function));
        return findById(createdFunction);
    }

    @Override
    public Function update(Function function) throws Exception {
        Function updatedFunction = jdbi.withExtension(FunctionDao.class, extension -> extension.update(function));
        if(updatedFunction == null)
            throw new IllegalArgumentException("Function in specified department does not exist: " + function.id());
        return findById(updatedFunction);
    }

    @Override
    public int delete(Function function) throws Exception {
        int result = jdbi.withExtension(FunctionDao.class, extension -> extension.delete(function));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Function does not exist: " + function.id());
    }

    @Override
    public List<Function> findAllForDepartment(Department department) throws Exception {
        return jdbi.withExtension(FunctionDao.class, extension -> extension.getFunctionsForDepartment(department));
    }

    @Override
    public Function findById(Function function) throws Exception {
        return jdbi.withExtension(FunctionDao.class, extension -> extension.findById(function));
    }
}
