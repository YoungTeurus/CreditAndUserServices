package modelconnectors;

import database.DataBaseConnectionException;
import models.Branch;
import models.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BranchDatabaseConnector extends BaseDatabaseConnector<Branch>{
    private static BranchDatabaseConnector instance;

    public static BranchDatabaseConnector getInstance() {
        if (instance == null) {
            instance = new BranchDatabaseConnector();
        }
        return instance;
    }

    @Override
    protected ResultSet getResultSetOfObjectOfId(long id) throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"branches\" WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        return db.executeStatement(preparedStatement);
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
    protected ResultSet getResultSetOfAllObjects() throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"branches\";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected ResultSet getResultSetOfAddedObjectId(Branch object) throws SQLException, DataBaseConnectionException {
        // Добавлять новые филиалы нельзя. (Временно?)
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        // Удалять филиалы нельзя.
        throw new UnsupportedOperationException();
    }
}
