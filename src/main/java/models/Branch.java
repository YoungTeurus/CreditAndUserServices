package models;

public class Branch extends AbstractModel {
    private long id;
    private String name;
    private String location;

    public Branch(long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
