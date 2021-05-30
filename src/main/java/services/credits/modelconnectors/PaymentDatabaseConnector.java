package services.credits.modelconnectors;

import database.DataBase;
import database.DataBaseConnectionException;
import database.PostgresDataBase;
import database.constructor.*;
import modelconnectors.BaseDatabaseConnector;
import services.credits.models.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDatabaseConnector extends BaseDatabaseConnector<Payment> {
    private PaymentDatabaseConnector(DataBase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "payments";
    }

    private static PaymentDatabaseConnector instance;

    public static PaymentDatabaseConnector getInstance() {
        if (instance == null) {
            // TODO: временное решение проблемы с базами данных в PaymentDatabaseConnector:
            DataBase db = PostgresDataBase.getCreditServiceInstance();
            instance = new PaymentDatabaseConnector(db);
        }
        return instance;
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
            e.printStackTrace();
        }
        return returnPayment;
    }

    @Override
    protected List<Parameter> getParametersForInsert(Payment payment) {
        List<Parameter> params = new ArrayList<>();

        params.add(new LongParameter("creditId", payment.getCreditId()));
        params.add(new DoubleParameter("sum", payment.getSum()));
        params.add(new DateParameter("date", Date.valueOf(payment.getDate())));

        return params;
    }

    public List<Payment> getByCreditId(int creditId) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new LongParameter("creditId", creditId));

        List<Payment> foundPayments = getByParameters(params);

        return foundPayments;
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        // Удалять записи о платежах нельзя.
        throw new UnsupportedOperationException();
    }
}