package carsharing.Entity;

public class Car extends DBObject {
    private int id;
    private String name;
    private int companyId;
    public Car(int id, String name, int companyId) {
        super();
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCompanyId() {
        return companyId;
    }
    @Override
    public String toString() {
        return id + ". " + name;
    }


}