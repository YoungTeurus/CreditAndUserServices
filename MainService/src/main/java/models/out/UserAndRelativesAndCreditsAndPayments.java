package models.out;

import java.util.List;

public class UserAndRelativesAndCreditsAndPayments {
    private final MainUserAndRelatives userAndRelatives;
    private final List<CreditAndPayments> credits;

    public UserAndRelativesAndCreditsAndPayments(MainUserAndRelatives user, List<CreditAndPayments> credits) {
        this.userAndRelatives = user;
        this.credits = credits;
    }

    public List<CreditAndPayments> getCredits() {
        return credits;
    }

    public MainUserAndRelatives getUserAndRelatives() {
        return userAndRelatives;
    }
}
