package modelconnectors;

import database.DataBaseConnectionException;
import models.Sex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SexDatabaseConnector extends BaseDatabaseConnector<Sex> {
    private static SexDatabaseConnector instance;

    public static SexDatabaseConnector getInstance() {
        if (instance == null) {
            instance = new SexDatabaseConnector();
        }
        return instance;
    }


    protected final ResultSet getResultSetOfObjectOfId(long id) throws DataBaseConnectionException, SQLException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"sexes\" WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected ResultSet getResultSetOfAllObjects() throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"sexes\";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected Sex constructObjectFromResultSet(ResultSet rs) {
        // Колонки, возвращаемые SQL запросом:
        // id, name
        Sex returnSex = null;
        try {
            returnSex = new Sex(
                    rs.getLong("id"),
                    rs.getString("name")
            );
        } catch (SQLException e) {
            // TODO: решить, как обрабатывать ошибку при невозможности создать sex из полученных данных.
            // Либо слать ошибку дальше по стеку вызовов, либо возвращать null.
        }
        return returnSex;
    }

    @Override
    protected ResultSet getResultSetOfAddedObjectId(Sex object) throws SQLException, DataBaseConnectionException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        throw new UnsupportedOperationException();
    }

}
