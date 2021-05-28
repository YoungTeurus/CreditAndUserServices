package modelconnectors;

import database.DataBase;
import database.DataBaseConnectionException;
import database.PostgresDataBase;
import database.constructor.BaseParameter;
import database.constructor.StatementConstructor;
import models.AbstractModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseDatabaseConnector<T extends AbstractModel> implements DatabaseConnector<T> {
    protected final DataBase db = PostgresDataBase.getInstance();

    @Override
    public final T get(long id) throws SQLException, DataBaseConnectionException {
        ResultSet resultSet = getResultSetOfObjectOfId(id);

        if (resultSet.next()) {
            return constructObjectFromResultSet(resultSet);
        }
        return null;
    }

    protected abstract ResultSet getResultSetOfObjectOfId(long id) throws SQLException, DataBaseConnectionException;

    protected abstract T constructObjectFromResultSet(ResultSet rs);

    // TODO: метод, который производит поиск по БД-шке по заданным полям
    public final List<T> find() throws SQLException, DataBaseConnectionException{
        return Collections.emptyList();
    }

    private ResultSet getResultSetOfFoundObjects(List<BaseParameter> parameters) throws SQLException, DataBaseConnectionException{
        Connection connection = db.getConnection();

        String sql = StatementConstructor.constructSelectSQLQuery("test", parameters);

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // preparedStatement.setLong(1, id);

        return db.executeStatement(preparedStatement);
    }

    @Override
    public final List<T> getAll() throws SQLException, DataBaseConnectionException {
        ResultSet resultSet = getResultSetOfAllObjects();

        List<T> objects = new ArrayList<>();
        while (resultSet.next()) {
            objects.add(constructObjectFromResultSet(resultSet));
        }
        return objects;
    }

    protected abstract ResultSet getResultSetOfAllObjects() throws SQLException, DataBaseConnectionException;


    @Override
    public final long addAndReturnId(T object) throws SQLException, DataBaseConnectionException {
        // Возвращаемые столбики SQL запроса:
        // без_названия (id)
        ResultSet rs = getResultSetOfAddedObjectId(object);
        if (rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }

    protected abstract ResultSet getResultSetOfAddedObjectId(T object) throws SQLException, DataBaseConnectionException;


    @Override
    public final boolean removeAndReturnSuccess(long id) throws SQLException, DataBaseConnectionException {
        ResultSet rs = getResultSetOfRemovedObjectId(id);

        // Если есть хотя бы одна строка, считаем, что удалили объект:
        if (rs.next()){
            return true;
        }
        return false;
    }

    @Override
    public final boolean removeAndReturnSuccess(T object) throws SQLException, DataBaseConnectionException {
        long objectId = object.getId();
        return removeAndReturnSuccess(objectId);
    }

    protected abstract ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException;
}
