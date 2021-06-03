package models;

import com.github.youngteurus.servletdatabase.models.AbstractModel;

public class MainServiceBranch extends AbstractModel {
    private final long id;
    private final String name;

    public MainServiceBranch(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
