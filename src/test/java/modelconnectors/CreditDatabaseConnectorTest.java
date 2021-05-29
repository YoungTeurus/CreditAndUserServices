package modelconnectors;

import database.DataBaseConnectionException;
import models.Credit;
import models.Payment;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditDatabaseConnectorTest {
    private CreditDatabaseConnector cdc = CreditDatabaseConnector.getInstance();

    @Test
    void getByUserId() throws SQLException, DataBaseConnectionException {
        List<Credit> credits = cdc.getByUserId(1);
        System.out.println(credits);
    }
}