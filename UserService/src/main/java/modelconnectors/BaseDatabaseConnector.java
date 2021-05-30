package modelconnectors;

import database.DataBase;
import database.DataBaseConnectionException;
import database.constructor.LongParameter;
import database.constructor.Parameter;
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
    protected DataBase db;

    protected BaseDatabaseConnector(DataBase db){
        this.db = db;
    }

    protected abstract String getTableName();

    public List<T> getByParameters(List<Parameter> parameters) throws SQLException, DataBaseConnectionException {
        ResultSet resultSet = getResultSetOfFoundObjectsByParameters(parameters);

        List<T> objects = new ArrayList<>();
        while (resultSet.next()) {
            objects.add(constructObjectFromResultSet(resultSet));
        }
        return objects;
    }

    private ResultSet getResultSetOfFoundObjectsByParameters(List<Parameter> parameters) throws SQLException, DataBaseConnectionException{
        Connection connection = db.getConnection();

        PreparedStatement preparedStatement = StatementConstructor.constructSelectStatementFromParametersList(
                connection, getTableName(), parameters
        );

        return db.executeStatement(preparedStatement);
    }

    protected abstract T constructObjectFromResultSet(ResultSet rs);

    @Override
    public final T getById(long id) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new LongParameter("id", id));

        List<T> objects = getByParameters(params);
        if(objects.size() > 0){
            return objects.get(0);
        }
        return null;
    }

    @Override
    public final List<T> getAll() throws SQLException, DataBaseConnectionException {
        @SuppressWarnings("UnnecessaryLocalVariable")
        List<T> objects = getByParameters(Collections.emptyList());

        return objects;
    }

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

    private ResultSet getResultSetOfAddedObjectId(T object) throws SQLException, DataBaseConnectionException{
        Connection connection = db.getConnection();

        List<Parameter> params = getParametersForInsert(object);

        PreparedStatement ps = StatementConstructor.constructInsertStatementFromParametersList(
                connection, getTableName(), params
        );

        return db.executeStatement(ps);
    }

    protected abstract List<Parameter> getParametersForInsert(T object);

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
