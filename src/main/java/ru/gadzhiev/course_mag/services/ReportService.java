package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.reports.Osv;

import java.util.Date;

public interface ReportService {

    Osv getOsv(final Date fromDate, final Date toDate, final String reverseType);

}
