package modelconnectors;

import database.DataBaseConnectionException;
import database.constructor.DateParameter;
import database.constructor.LongParameter;
import database.constructor.Parameter;
import database.constructor.StringParameter;
import models.Branch;
import models.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDatabaseConnector extends BaseDatabaseConnector<Branch>{
    @Override
    protected String getTableName() {
        return "branches";
    }

    private static BranchDatabaseConnector instance;

    public static BranchDatabaseConnector getInstance() {
        if (instance == null) {
            instance = new BranchDatabaseConnector();
        }
        return instance;
    }

    @Override
    protected Branch constructObjectFromResultSet(ResultSet rs) {
        // Колонки, возвращаемые SQL запросом:
        // id, name, location
        Branch returnBranch = null;
        try {
            returnBranch = new Branch(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("location")
            );
        } catch (SQLException e) {
            // TODO: решить, как обрабатывать ошибку при невозможности создать sex из полученных данных.
            // Либо слать ошибку дальше по стеку вызовов, либо возвращать null.
        }
        return returnBranch;
    }

    @Override
    protected List<Parameter> getParametersForInsert(Branch branch) {
        // Добавлять новые филиалы нельзя. (Временно?)
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        // Удалять филиалы нельзя.
        throw new UnsupportedOperationException();
    }
}
