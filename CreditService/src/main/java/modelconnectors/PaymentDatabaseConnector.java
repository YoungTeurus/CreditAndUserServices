package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.BigDecimalParameter;
import com.github.youngteurus.servletdatabase.database.constructor.DateParameter;
import com.github.youngteurus.servletdatabase.database.constructor.LongParameter;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.modelconnectors.AbstractModelDatabaseConnector;
import database.CreditPostgresDataBase;
import models.Payment;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDatabaseConnector extends AbstractModelDatabaseConnector<Payment> {
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
            DataBase db = CreditPostgresDataBase.getInstance();
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
                    rs.getBigDecimal("sum"),
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
        params.add(new BigDecimalParameter("sum", payment.getSum()));
        params.add(new DateParameter("date", Date.valueOf(payment.getDate())));

        return params;
    }

    public List<Payment> getByCreditId(long creditId) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new LongParameter("creditId", creditId));

        return getByParameters(params);
    }

    @Override
    protected List<Parameter> getParametersForRemove(Payment payment) {
        throw new UnsupportedOperationException();
    }
}
