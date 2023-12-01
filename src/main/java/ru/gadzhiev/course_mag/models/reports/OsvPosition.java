package ru.gadzhiev.course_mag.models.reports;

import ru.gadzhiev.course_mag.models.Account;

public final class OsvPosition {
        private Account account;
        private double startDebit;
        private double startCredit;
        private double periodDebit;
        private double periodCredit;
        private double endDebit;
        private double endCredit;

    public OsvPosition(Account account, double startDebit, double startCredit, double periodDebit, double periodCredit, double endDebit, double endCredit) {
        this.account = account;
        this.startDebit = startDebit;
        this.startCredit = startCredit;
        this.periodDebit = periodDebit;
        this.periodCredit = periodCredit;
        this.endDebit = endDebit;
        this.endCredit = endCredit;
    }

    public double getStartDebit() {
        return startDebit;
    }

    public void setStartDebit(double startDebit) {
        this.startDebit = startDebit;
    }

    public double getStartCredit() {
        return startCredit;
    }

    public void setStartCredit(double startCredit) {
        this.startCredit = startCredit;
    }

    public double getPeriodDebit() {
        return periodDebit;
    }

    public void setPeriodDebit(double periodDebit) {
        this.periodDebit = periodDebit;
    }

    public double getPeriodCredit() {
        return periodCredit;
    }

    public void setPeriodCredit(double periodCredit) {
        this.periodCredit = periodCredit;
    }

    public double getEndDebit() {
        return endDebit;
    }

    public void setEndDebit(double endDebit) {
        this.endDebit = endDebit;
    }

    public double getEndCredit() {
        return endCredit;
    }

    public void setEndCredit(double endCredit) {
        this.endCredit = endCredit;
    }

    public Account getAccount() {
        return account;
    }
}
