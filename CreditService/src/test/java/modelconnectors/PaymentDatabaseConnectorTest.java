package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import models.Payment;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PaymentDatabaseConnectorTest {
    private final PaymentDatabaseConnector pdc = PaymentDatabaseConnector.getInstance();

    @Test
    void getByCreditId() throws SQLException, DataBaseConnectionException {
        List<Payment> payments = pdc.getByCreditId(2);
        System.out.println(payments);

        assertNotEquals(0, payments.size());
    }
}