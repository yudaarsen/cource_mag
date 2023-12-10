package ru.gadzhiev.course_mag.services;

import ru.gadzhiev.course_mag.models.reports.BalanceRow;
import ru.gadzhiev.course_mag.models.reports.Osv;

import java.util.Date;
import java.util.List;

public interface ReportService {

    Osv getOsv(final Date fromDate, final Date toDate, final String reverseType);

    List<BalanceRow> getBalance(final Date fromDate, final Date toDate);
}
