package modelconnectors;

import database.DataBaseConnectionException;
import models.Payment;

import java.sql.*;

public class PaymentDatabaseConnector extends BaseDatabaseConnector<Payment>{
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

        String sql = "SELECT * FROM public.\"payments\" WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected Payment constructObjectFromResultSet(ResultSet rs) {
        // Колонки, возвращаемые SQL запросом:
        // id, creditId, sum, date
        Payment returnPayment = null;
        try {
            returnPayment = new Payment(
                    rs.getLong("id"),
                    rs.getLong("creditId"),
                    rs.getDouble("sum"),
                    rs.getDate("date").toLocalDate()
            );
        } catch (SQLException e) {
            // TODO: решить, как обрабатывать ошибку при невозможности создать sex из полученных данных.
            // Либо слать ошибку дальше по стеку вызовов, либо возвращать null.
        }
        return returnPayment;
    }

    @Override
    protected ResultSet getResultSetOfAllObjects() throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"payments\";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected ResultSet getResultSetOfAddedObjectId(Payment payment) throws SQLException, DataBaseConnectionException {
        String sql = "INSERT INTO public.\"payments\" (\"creditId\", \"sum\", \"date\")" +
                " Values (?, ?, ?) RETURNING id;";
        Connection connection = db.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, payment.getCreditId());
        preparedStatement.setDouble(2, payment.getSum());
        preparedStatement.setDate(3, Date.valueOf(payment.getDate()));

        return db.executeStatement(preparedStatement);
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        // Удалять записи о платежах нельзя.
        throw new UnsupportedOperationException();
    }
}
