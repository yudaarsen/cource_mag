package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.ReportDao;
import ru.gadzhiev.course_mag.models.Account;
import ru.gadzhiev.course_mag.models.DocumentType;
import ru.gadzhiev.course_mag.models.reports.*;

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
                pos.setEndDebit(Math.max(0, pos.getStartDebit() + pos.getPeriodDebit() - pos.getPeriodCredit() - pos.getStartCredit()));
            else
                pos.setEndCredit(Math.max(0, pos.getStartCredit() + pos.getPeriodCredit() - pos.getPeriodDebit() - pos.getStartDebit()));
        }

        double prevTotalsDebit = 0;
        double prevTotalsCredit = 0;
        double endTotalsDebit = 0;
        double endTotalsCredit = 0;

        for(OsvPosition pos : result) {
            if(pos.getAccount().parent() != null)
                continue;
            prevTotalsDebit += pos.getStartDebit();
            prevTotalsCredit += pos.getStartCredit();
            endTotalsDebit += pos.getEndDebit();
            endTotalsCredit += pos.getEndCredit();
        }

        return new Osv(
                result,
                jdbi.withExtension(ReportDao.class, extension -> extension.getOsvTotals(fromDate, toDate, reverseType)),
                new OsvTotals(prevTotalsDebit, prevTotalsCredit),
                new OsvTotals(endTotalsDebit, endTotalsCredit)
        );
    }

    @Override
    public List<BalanceRow> getBalance(Date fromDate, Date toDate) {
        Osv osv = getOsv(fromDate, toDate, DocumentType.TYPE_REVERSE);
        double[] account_01 = getAccountData(osv, Account.ACCOUNT_01);
        double[] account_02 = getAccountData(osv, Account.ACCOUNT_02);
        double[] account_10 = getAccountData(osv, Account.ACCOUNT_10);
        double[] account_20 = getAccountData(osv, Account.ACCOUNT_20);
        double[] account_43 = getAccountData(osv, Account.ACCOUNT_43);
        double[] account_51 = getAccountData(osv, Account.ACCOUNT_51);
        double[] account_76 = getAccountData(osv, Account.ACCOUNT_76);
        double[] account_80 = getAccountData(osv, Account.ACCOUNT_80);
        double[] account_84 = getAccountData(osv, Account.ACCOUNT_84);
        double[] account_99 = getAccountData(osv, Account.ACCOUNT_99);

        double debitStart_1230 = 0;
        double debitEnd_1230 = 0;
        double creditStart_1520 = 0;
        double creditEnd_1520 = 0;

        for(OsvPosition position : osv.positions()) {
            if(position.getAccount().code().equals(Account.ACCOUNT_601)
                || position.getAccount().code().equals(Account.ACCOUNT_602)
                || position.getAccount().code().equals(Account.ACCOUNT_621)
                || position.getAccount().code().equals(Account.ACCOUNT_622)
                || position.getAccount().code().equals(Account.ACCOUNT_68)
                || position.getAccount().code().equals(Account.ACCOUNT_69)
                || position.getAccount().code().equals(Account.ACCOUNT_70)) {
                debitStart_1230 += position.getStartDebit();
                debitEnd_1230 += position.getEndDebit();
                creditStart_1520 += position.getStartCredit();
                creditEnd_1520 += position.getEndCredit();
            }
        }

        List<BalanceRow> result = new ArrayList<>();
        result.add(new BalanceRow("Нематериальные активы", 1110, 0, 0));
        result.add(new BalanceRow("Результаты исследовательских работ и разработок", 1120, 0, 0));
        result.add(new BalanceRow("Нематериальные поисковые активы", 1130, 0, 0));
        result.add(new BalanceRow("Материальные поисковые активы", 1140, 0, 0));
        result.add(new BalanceRow("Основные средства", 1150,
                (int)(account_01[0] - account_02[1]) / 1000,
                (int)(account_01[2] - account_02[3]) / 1000));
        result.add(new BalanceRow("Доходные вложения в материальные ценности", 1160, 0, 0));
        result.add(new BalanceRow("Финансовые вложения", 1170, 0, 0));
        result.add(new BalanceRow("Отложенные налоговые активы", 1180, 0, 0));
        result.add(new BalanceRow("Иные внеоборотные активы", 1190, 0, 0));
        result.add(new BalanceRow("Итого по разделу I", 1100,
                (int)(account_01[0] - account_02[1]) / 1000,
                (int)(account_01[2] - account_02[3]) / 1000));

        result.add(new BalanceRow("Запасы", 1210,
                (int)(account_10[0] + account_20[0] + account_43[0]) / 1000,
                (int)(account_01[2] + account_02[2] + account_43[2]) / 1000));
        result.add(new BalanceRow("НДС по приобретенным ценностям", 1220, 0, 0));
        result.add(new BalanceRow("Дебиторская задолженность", 1230,
                (int)(debitStart_1230) / 1000,
                (int)(debitEnd_1230) / 1000));
        result.add(new BalanceRow("Финансовые вложения, исключая денежные", 1240, 0, 0));
        result.add(new BalanceRow("Денежные средства и эквиваленты", 1250,
                (int)(account_51[0]) / 1000,
                (int)(account_51[2]) / 1000));
        result.add(new BalanceRow("Иные оборотные активы", 1260, 0, 0));
        result.add(new BalanceRow("Итого по разделу II", 1200,
                (int)(account_10[0] + account_20[0] + account_43[0] + debitStart_1230 + account_51[0]) / 1000,
                (int)(account_10[2] + account_20[2] + account_43[2] + debitEnd_1230 + account_51[2]) / 1000)
        );
        result.add(new BalanceRow("Баланс", 1600,
                (int)(account_10[0] + account_20[0] + account_43[0] + debitStart_1230 + account_51[0] + account_01[0] - account_02[1]) / 1000,
                (int)(account_10[2] + account_20[2] + account_43[2] + debitEnd_1230 + account_51[2] + account_01[2] - account_02[3]) / 1000)
        );

        result.add(new BalanceRow("Уставный капитал", 1310,
                (int)(account_80[1]) / 1000,
                (int)(account_80[3]) / 1000));
        result.add(new BalanceRow("Акции компании, выкупленные у владельцев акций", 1320, 0, 0));
        result.add(new BalanceRow("Переоценка внеоборотных активов", 1340, 0, 0));
        result.add(new BalanceRow("Добавочный капитал (без проведения переоценки)", 1350, 0, 0));
        result.add(new BalanceRow("Резервный капитал", 1360, 0, 0));
        result.add(new BalanceRow("Нераспределенная прибыль (непокрытые убытки)", 1370,
                (int)(account_84[1] + account_99[1]) / 1000,
                (int)(account_84[3] + account_99[3]) / 1000));
        result.add(new BalanceRow("Итого по разделу III", 1300,
                (int)(account_80[1] + account_84[1] + account_99[1]) / 1000,
                (int)(account_80[3] + account_84[3] + account_99[3]) / 1000));

        result.add(new BalanceRow("Заёмные средства", 1410, 0, 0));
        result.add(new BalanceRow("Отложенные налоговые обязательства", 1420, 0, 0));
        result.add(new BalanceRow("Оценочные обязательства", 1430, 0, 0));
        result.add(new BalanceRow("Иные обязательства", 1450,
                (int)(account_76[1]) / 1000,
                (int)(account_76[3]) / 1000));
        result.add(new BalanceRow("Итого по разделу IV", 1400,
                (int)(account_76[1]) / 1000,
                (int)(account_76[3]) / 1000));

        result.add(new BalanceRow("Заёмные средства", 1510, 0, 0));
        result.add(new BalanceRow("Кредиторская задолженность", 1520,
                (int)(creditStart_1520) / 1000,
                (int)(creditEnd_1520) / 1000)
        );
        result.add(new BalanceRow("Доходы будущих периодов", 1530, 0, 0));
        result.add(new BalanceRow("Оценочные обязательства", 1540, 0, 0));
        result.add(new BalanceRow("Прочие краткосрочные обязательства", 1550, 0, 0));
        result.add(new BalanceRow("Итого по разделу V", 1500,
                (int)(creditStart_1520) / 1000,
                (int)(creditEnd_1520) / 1000)
        );
        result.add(new BalanceRow("БАЛАНС", 1700,
                (int)(account_80[1] + account_84[1] + account_99[1] + account_76[1] + creditStart_1520) / 1000,
                (int)(account_80[3] + account_84[3] + account_99[3] + account_76[3] + creditEnd_1520) / 1000)
        );
        return result;
    }

    @Override
    public List<VedRow> getVed(Date fromDate, Date toDate) {
        return jdbi.withExtension(ReportDao.class, extension -> extension.getVed(fromDate, toDate));
    }

    private double[] getAccountData(final Osv osv, String accountCode) {
        double[] result = {0, 0, 0, 0}; // startDebit, startCredit, endDebit, endCredit
        for(OsvPosition position : osv.positions()) {
            if(position.getAccount().code().equals(accountCode)) {
                result[0] = position.getStartDebit();
                result[1] = position.getStartCredit();
                result[2] = position.getEndDebit();
                result[3] = position.getEndCredit();
                return result;
            }
        }
        return result;
    }
}
