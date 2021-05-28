package modelconnectors;

import database.DataBaseConnectionException;
import database.constructor.Parameter;
import models.Sex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SexDatabaseConnector extends BaseDatabaseConnector<Sex> {
    @Override
    protected String getTableName() {
        return "sexes";
    }

    private static SexDatabaseConnector instance;

    public static SexDatabaseConnector getInstance() {
        if (instance == null) {
            instance = new SexDatabaseConnector();
        }
        return instance;
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
    protected List<Parameter> getParametersForInsert(Sex object) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        throw new UnsupportedOperationException();
    }

}
