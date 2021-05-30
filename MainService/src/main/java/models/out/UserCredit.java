package models.out;

import models.Credit;
import models.User;

import java.util.List;

public class UserCredit {
    private final User user;
    private final List<Credit> credits;

    public UserCredit(User user, List<Credit> credits) {
        this.user = user;
        this.credits = credits;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public User getUser() {
        return user;
    }
}
