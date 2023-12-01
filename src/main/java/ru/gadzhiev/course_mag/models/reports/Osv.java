package ru.gadzhiev.course_mag.models.reports;

import java.util.List;

public record Osv(
        List<OsvPosition> positions,
        OsvTotals totals
)
{}
