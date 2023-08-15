package carsharing.Abstractions;

import java.util.List;

public interface AbstractDAO <DBObject>{
    List<DBObject> getByName(String name);
    List<DBObject> getAll();
    void create(DBObject entity);
}
