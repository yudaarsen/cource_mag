package ru.gadzhiev.course_mag.models.reports;

public record VedRow (
    int personnelNumber,
    String fio,
    String fname,
    long salary,
    int present
)
{}
