package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.Department;
import ru.gadzhiev.course_mag.models.Function;

import java.util.List;

public interface FunctionService {
    Function create(final Function function) throws Exception;
    Function update(final Function function) throws Exception;
    int delete(final Function function) throws Exception;
    List<Function> findAllForDepartment(final Department department) throws Exception;
}
