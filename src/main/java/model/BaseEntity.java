package model;

public abstract class BaseEntity {
    protected int id;
    protected String name;

    protected BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected BaseEntity(String name) {
        this.name = name;
    }

    // concrete method
    public String displayName() {
        return "[" + getEntityType() + "] " + id + " | " + name;
    }

    // abstract methods
    public abstract String getEntityType();
    public abstract String describe();

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
