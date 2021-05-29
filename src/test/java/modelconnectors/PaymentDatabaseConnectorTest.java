package modelconnectors;

import database.DataBaseConnectionException;
import models.Payment;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDatabaseConnectorTest {
    private final PaymentDatabaseConnector pdc = PaymentDatabaseConnector.getInstance();

    @Test
    void getByCreditId() throws SQLException, DataBaseConnectionException {
        List<Payment> payments = pdc.getByCreditId(2);
        System.out.println(payments);
    }
}