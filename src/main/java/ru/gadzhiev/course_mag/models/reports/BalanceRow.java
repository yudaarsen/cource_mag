package ru.gadzhiev.course_mag.models.reports;

public record BalanceRow(
        String name,
        int code,
        int start,
        int end
)
{}