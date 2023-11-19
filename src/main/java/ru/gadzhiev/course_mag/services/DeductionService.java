package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.Deduction;

import java.util.List;

public interface DeductionService {
    Deduction create(final Deduction deduction) throws Exception;

    int delete(final Deduction deduction) throws Exception;

    List<Deduction> findAll() throws Exception;
}
