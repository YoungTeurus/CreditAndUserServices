package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.modelconnectors.AbstractModelDatabaseConnector;
import database.CreditPostgresDataBase;
import models.Sex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SexDatabaseConnector extends AbstractModelDatabaseConnector<Sex> {
    private static SexDatabaseConnector instance;

    private SexDatabaseConnector(DataBase db){
        super(db);
    }

    public static SexDatabaseConnector getInstance() {
        if (instance == null) {
            DataBase db = CreditPostgresDataBase.getInstance();
            instance = new SexDatabaseConnector(db);
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return "sexes";
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
    protected List<Parameter> getParametersForRemove(Sex sex) {
        throw new UnsupportedOperationException();
    }
}
