package services.users.models;

import models.AbstractModel;

public class Sex extends AbstractModel {
    private final long id;
    private final String name;

    public Sex(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Sex{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}