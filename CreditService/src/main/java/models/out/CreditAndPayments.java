package models.out;

import models.Credit;
import models.Payment;

import java.util.List;

public class CreditAndPayments {
    private final Credit credit;
    private final List<Payment> payments;

    public CreditAndPayments(Credit credit, List<Payment> payments) {
        this.credit = credit;
        this.payments = payments;
    }

    public Credit getCredit() {
        return credit;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    @Override
    public String toString() {
        return "CreditAndPayments{" +
                "credit=" + credit +
                ", payments=" + payments +
                '}';
    }
}
