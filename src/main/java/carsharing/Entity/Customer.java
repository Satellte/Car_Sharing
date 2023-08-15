package carsharing.Entity;

public class Customer extends DBObject {
    private int id;
    private String name;
    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Customer(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return id + ". " + name;
    }
}
