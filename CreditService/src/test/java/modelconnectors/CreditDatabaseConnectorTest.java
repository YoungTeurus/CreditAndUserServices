package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import models.Credit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

class CreditDatabaseConnectorTest {
    private final CreditDatabaseConnector cdc = CreditDatabaseConnector.getInstance();

    @Test
    void getCreditsByUserId() throws SQLException, DataBaseConnectionException {
        List<Credit> credits = cdc.getByUserId(1);
        System.out.println(credits);

        assertNotEquals(0, credits.size());
    }
}