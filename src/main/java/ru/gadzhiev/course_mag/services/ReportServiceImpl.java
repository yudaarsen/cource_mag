package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.ReportDao;
import ru.gadzhiev.course_mag.models.Account;
import ru.gadzhiev.course_mag.models.DocumentType;
import ru.gadzhiev.course_mag.models.reports.BalanceRow;
import ru.gadzhiev.course_mag.models.reports.Osv;
import ru.gadzhiev.course_mag.models.reports.OsvPosition;
import ru.gadzhiev.course_mag.models.reports.VedRow;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    @Override
    public List<BalanceRow> getBalance(Date fromDate, Date toDate) {
        Osv osv = getOsv(fromDate, toDate, DocumentType.TYPE_REVERSE);
        int[] account_20 = getAccountData(osv, Account.ACCOUNT_20);
        int[] account_26 = getAccountData(osv, Account.ACCOUNT_26);
        int[] account_51 = getAccountData(osv, Account.ACCOUNT_51);
        int[] account_68 = getAccountData(osv, Account.ACCOUNT_68);
        int[] account_69 = getAccountData(osv, Account.ACCOUNT_69);
        int[] account_70 = getAccountData(osv, Account.ACCOUNT_70);
        int[] account_76 = getAccountData(osv, Account.ACCOUNT_76);

        List<BalanceRow> result = new ArrayList<>();
        result.add(new BalanceRow("Нематериальные активы", 1110, 0, 0));
        result.add(new BalanceRow("Результаты исследовательских работ и разработок", 1120, 0, 0));
        result.add(new BalanceRow("Нематериальные поисковые активы", 1130, 0, 0));
        result.add(new BalanceRow("Материальные поисковые активы", 1140, 0, 0));
        result.add(new BalanceRow("Основные средства", 1150, 0, 0));
        result.add(new BalanceRow("Доходные вложения в материальные ценности", 1160, 0, 0));
        result.add(new BalanceRow("Финансовые вложения", 1170, 0, 0));
        result.add(new BalanceRow("Отложенные налоговые активы", 1180, 0, 0));
        result.add(new BalanceRow("Иные внеоборотные активы", 1190, 0, 0));
        result.add(new BalanceRow("Итого по разделу I", 1100, 0, 0));

        result.add(new BalanceRow("Запасы", 1210, account_20[0], account_20[2]));
        result.add(new BalanceRow("НДС по приобретенным ценностям", 1220, 0, 0));
        result.add(new BalanceRow("Дебиторская задолженность", 1230, account_68[0] + account_69[0], account_68[2] + account_69[2]));
        result.add(new BalanceRow("Финансовые вложения, исключая денежные", 1240, 0, 0));
        result.add(new BalanceRow("Денежные средства и эквиваленты", 1250, account_51[0], account_51[2]));
        result.add(new BalanceRow("Иные оборотные активы", 1260, 0, 0));
        result.add(new BalanceRow("Итого по разделу II", 1200,
                account_20[0] + account_68[0] + account_69[0] + account_51[0],
                account_20[2] + account_68[2] + account_69[2] + account_51[2])
        );
        result.add(new BalanceRow("Баланс", 1600,
                account_20[0] + account_68[0] + account_69[0] + account_51[0],
                account_20[2] + account_68[2] + account_69[2] + account_51[2])
        );

        result.add(new BalanceRow("Уставный капитал", 1310, 0, 0));
        result.add(new BalanceRow("Акции компании, выкупленные у владельцев акций", 1320, 0, 0));
        result.add(new BalanceRow("Переоценка внеоборотных активов", 1340, 0, 0));
        result.add(new BalanceRow("Добавочный капитал (без проведения переоценки)", 1350, 0, 0));
        result.add(new BalanceRow("Резервный капитал", 1360, 0, 0));
        result.add(new BalanceRow("Нераспределенная прибыль (непокрытые убытки)", 1370, 0, 0));
        result.add(new BalanceRow("Итого по разделу III", 1300, 0, 0));

        result.add(new BalanceRow("Заёмные средства", 1410, 0, 0));
        result.add(new BalanceRow("Отложенные налоговые обязательства", 1420, 0, 0));
        result.add(new BalanceRow("Оценочные обязательства", 1430, 0, 0));
        result.add(new BalanceRow("Иные обязательства", 1450, account_76[1], account_76[3]));
        result.add(new BalanceRow("Итого по разделу IV", 1400, account_76[1], account_76[3]));

        result.add(new BalanceRow("Заёмные средства", 1510, 0, 0));
        result.add(new BalanceRow("Кредиторская задолженность", 1520,
                account_68[1] + account_69[1] + account_70[1],
                account_68[3] + account_69[3] + account_70[3])
        );
        result.add(new BalanceRow("Доходы будущих периодов", 1530, 0, 0));
        result.add(new BalanceRow("Оценочные обязательства", 1540, 0, 0));
        result.add(new BalanceRow("Прочие краткосрочные обязательства", 1550, 0, 0));
        result.add(new BalanceRow("Итого по разделу V", 1500,
                account_68[1] + account_69[1] + account_70[1] + account_76[1],
                account_68[3] + account_69[3] + account_70[3] + account_76[3])
        );
        result.add(new BalanceRow("БАЛАНС", 1700,
                account_76[1] + account_68[1] + account_69[1] + account_70[1],
                account_76[3] + account_68[3] + account_69[3] + account_70[3])
        );
        return result;
    }

    @Override
    public List<VedRow> getVed(Date fromDate, Date toDate) {
        return jdbi.withExtension(ReportDao.class, extension -> extension.getVed(fromDate, toDate));
    }

    private int[] getAccountData(final Osv osv, String accountCode) {
        int[] result = {0, 0, 0, 0}; // startDebit, startCredit, endDebit, endCredit
        for(OsvPosition position : osv.positions()) {
            if(position.getAccount().code().equals(accountCode)) {
                result[0] = (int)position.getStartDebit() / 1000;
                result[1] = (int)position.getStartCredit() / 1000;
                result[2] = (int)position.getEndDebit() / 1000;
                result[3] = (int)position.getEndCredit() / 1000;
                return result;
            }
        }
        return result;
    }
}
