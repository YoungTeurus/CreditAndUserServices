package models.out;

import models.User;

import java.util.List;

public class UserCredit {
    private final User user;
    private final List<CreditAndPayments> credits;

    public UserCredit(User user, List<CreditAndPayments> credits) {
        this.user = user;
        this.credits = credits;
    }

    public List<CreditAndPayments> getCredits() {
        return credits;
    }

    public User getUser() {
        return user;
    }
}
