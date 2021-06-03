package models.out;

import models.User;

import java.util.List;

public class UserAndRelatives {
    private final User user;
    private final List<User> parents;
    private final List<User> children;
    private final List<User> siblings;

    public UserAndRelatives(User user, List<User> parents, List<User> children, List<User> siblings) {
        this.user = user;
        this.parents = parents;
        this.children = children;
        this.siblings = siblings;
    }

    public User getUser() {
        return user;
    }

    public List<User> getParents() {
        return parents;
    }

    public List<User> getChildren() {
        return children;
    }

    public List<User> getSiblings() {
        return siblings;
    }

    @Override
    public String toString() {
        return "UserAndRelatives{" +
                "user=" + user +
                ", parents=" + parents +
                ", children=" + children +
                ", siblings=" + siblings +
                '}';
    }
}
