package modelconnectors;

import database.DataBaseConnectionException;
import services.credits.modelconnectors.CreditDatabaseConnector;
import services.credits.models.Credit;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

class CreditDatabaseConnectorTest {
    private CreditDatabaseConnector cdc = CreditDatabaseConnector.getInstance();

    @Test
    void getCreditsByUserId() throws SQLException, DataBaseConnectionException {
        List<Credit> credits = cdc.getByUserId(1);
        System.out.println(credits);
    }
}