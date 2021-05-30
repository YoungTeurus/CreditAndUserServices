package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.modelconnectors.BaseDatabaseConnector;
import database.CreditPostgresDataBase;
import models.Sex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SexDatabaseConnector extends BaseDatabaseConnector<Sex> {
    private SexDatabaseConnector(DataBase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "sexes";
    }

    private static SexDatabaseConnector instance;

    public static SexDatabaseConnector getInstance() {
        if (instance == null) {
            // TODO: временное решение проблемы с базами данных в SexDatabaseConnector:
            DataBase db = CreditPostgresDataBase.getInstance();
            instance = new SexDatabaseConnector(db);
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
            e.printStackTrace();
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
