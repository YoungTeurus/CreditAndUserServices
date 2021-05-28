package modelconnectors;

import database.DataBaseConnectionException;
import models.Credit;

import java.sql.*;

public class CreditDatabaseConnector extends BaseDatabaseConnector<Credit>{
    private static CreditDatabaseConnector instance;

    public static CreditDatabaseConnector getInstance() {
        if (instance == null) {
            instance = new CreditDatabaseConnector();
        }
        return instance;
    }

    @Override
    protected ResultSet getResultSetOfObjectOfId(long id) throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"credits\" WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected ResultSet getResultSetOfAllObjects() throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"credits\";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected Credit constructObjectFromResultSet(ResultSet rs) {
        // Колонки, возвращаемые SQL запросом:
        // id, userId, totalSum, startPaymentDate, endPaymentDate, branchId
        Credit returnCredit = null;
        try {
            returnCredit = new Credit.Builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getLong("userId"))
                    .branchId(rs.getLong("branchId"))
                    .totalSum(rs.getDouble("totalSum"))
                    .startPaymentDate(rs.getDate("startPaymentDate").toLocalDate())
                    .endPaymentDate(rs.getDate("endPaymentDate").toLocalDate())
                    .build();
        } catch (SQLException e) {
            // TODO: решить, как обрабатывать ошибку при невозможности создать sex из полученных данных.
            // Либо слать ошибку дальше по стеку вызовов, либо возвращать null.
        }
        return returnCredit;
    }

    @Override
    protected ResultSet getResultSetOfAddedObjectId(Credit credit) throws SQLException, DataBaseConnectionException {
        String sql = "INSERT INTO public.\"credits\" (\"userId\", \"totalSum\", \"startPaymentDate\", \"endPaymentDate\", \"branchId\")" +
                " Values (?, ?, ?, ?, ?) RETURNING id;";
        Connection connection = db.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, credit.getUserId());
        preparedStatement.setDouble(2, credit.getTotalSum());
        preparedStatement.setDate(3, Date.valueOf(credit.getStartPaymentDate()));
        preparedStatement.setDate(4, Date.valueOf(credit.getEndPaymentDate()));
        preparedStatement.setLong(5, credit.getBranchId());

        return db.executeStatement(preparedStatement);
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        // Удалять записи о кредитах нельзя.
        throw new UnsupportedOperationException();
    }
}
