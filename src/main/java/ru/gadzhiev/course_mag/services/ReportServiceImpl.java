package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.ReportDao;
import ru.gadzhiev.course_mag.models.reports.Osv;
import ru.gadzhiev.course_mag.models.reports.OsvPosition;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private Jdbi jdbi;

    @Override
    public Osv getOsv(Date fromDate, Date toDate, String reverseType) {
        List<OsvPosition> result = jdbi.withExtension(ReportDao.class, extension -> extension.getOsv(fromDate, toDate, reverseType));

        Date prevDate = Date.from(fromDate.toInstant().minus(1, ChronoUnit.DAYS));
        List<OsvPosition> prevPeriodOsv = jdbi.withExtension(
                ReportDao.class, extension -> extension.getOsv(new Date(0), prevDate, reverseType)
        );

        for(OsvPosition pos : result) {
            for(OsvPosition prevPos : prevPeriodOsv) {
                if(pos.getAccount().code().equals(prevPos.getAccount().code())) {
                    pos.setStartDebit(Math.max(0, prevPos.getPeriodDebit() - prevPos.getPeriodCredit()));
                    pos.setStartCredit(Math.max(0, prevPos.getPeriodCredit() - prevPos.getPeriodDebit()));
                    break;
                }
            }
            if(pos.getStartDebit() + pos.getPeriodDebit() > pos.getStartCredit() + pos.getPeriodCredit())
                pos.setEndDebit(Math.max(0, pos.getStartDebit() + pos.getPeriodDebit() - pos.getPeriodCredit()));
            else
                pos.setEndCredit(Math.max(0, pos.getStartCredit() + pos.getPeriodCredit() - pos.getPeriodDebit()));
        }

        return new Osv(
                result,
                jdbi.withExtension(ReportDao.class, extension -> extension.getOsvTotals(fromDate, toDate, reverseType)),
                jdbi.withExtension(ReportDao.class, extension -> extension.getOsvTotals(new Date(0), prevDate, reverseType)),
                jdbi.withExtension(ReportDao.class, extension -> extension.getOsvTotals(new Date(0), toDate, reverseType))
        );
    }
}
