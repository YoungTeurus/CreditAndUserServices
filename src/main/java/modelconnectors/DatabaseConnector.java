package modelconnectors;

import database.DataBaseConnectionException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseConnector<T> {
    T get(long id) throws SQLException, DataBaseConnectionException;
    List<T> getAll() throws SQLException, DataBaseConnectionException;
    long addAndReturnId(T object) throws SQLException, DataBaseConnectionException;
    boolean removeAndReturnSuccess(long id) throws SQLException, DataBaseConnectionException;
    boolean removeAndReturnSuccess(T object) throws SQLException, DataBaseConnectionException;
}
