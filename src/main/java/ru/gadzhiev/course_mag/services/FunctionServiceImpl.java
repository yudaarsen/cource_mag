package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.Function;

import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private Jdbi jdbi;

    @Override
    public Function create(Function function) throws Exception {
        return null;
    }

    @Override
    public Function update(Function function) throws Exception {
        return null;
    }

    @Override
    public int delete(Function function) throws Exception {
        return 0;
    }

    @Override
    public List<Function> findAllForDepartment(Department department) throws Exception {
        return null;
    }
}
