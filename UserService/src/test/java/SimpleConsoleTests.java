import database.DataBaseConnectionException;
import modelconnectors.SexDatabaseConnector;
import modelconnectors.UserDatabaseConnector;
import models.Sex;
import models.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleConsoleTests {
    private final UserDatabaseConnector udc = UserDatabaseConnector.getInstance();
    private final User testUser = new User.Builder("TEST_USER").driverLicenceId("1111").build();

    @Test
    void addWithEveryField() throws SQLException, DataBaseConnectionException {
        SexDatabaseConnector sdc = SexDatabaseConnector.getInstance();
        List<Sex> sexes = sdc.getAll();

        User u = new User.Builder("Adam")
               .surname("Petrovich")
               .birthDate(LocalDate.now())
               .passportNumber("4231 543213")
               .taxPayerID("984454321")
               .driverLicenceId("123999789")
               .sex(sexes.get(2))
               .build();
        System.out.println(u);
        try {
            long addedId = udc.addAndReturnId(u);
            System.out.printf("Added user with ID %d\n", addedId);
        } catch (DataBaseConnectionException e){
            // Что-то не так с соединением:
            e.printStackTrace();
        } catch (SQLException e){
            // Что-то не так с SQL-запросом:
            e.printStackTrace();
        }
    }

    @Test
    void add() throws SQLException, DataBaseConnectionException {
        System.out.println(testUser);
        try {
            long addedId = udc.addAndReturnId(testUser);
            System.out.printf("Added user with ID %d\n", addedId);
        } catch (DataBaseConnectionException e){
            // Что-то не так с соединением:
            e.printStackTrace();
        } catch (SQLException e){
            // Что-то не так с SQL-запросом:
            e.printStackTrace();
        }
    }

    @Test
    void select() throws SQLException, DataBaseConnectionException{
        List<User> users = udc.getAll();
        for(User u : users){
            System.out.println(u);
        }
    }

//    @Test
//    void remove() throws SQLException, DataBaseConnectionException {
//        User u = new User.Builder("USER_TO_REMOVE")
//                .driverLicenceId("1111").build();
//        System.out.println(u);
//
//        long addedId = udc.addAndReturnId(u);
//        System.out.printf("Added user with ID %d\n", addedId);
//
//        // Заменяем default id = 0 на реальный.
//        u = udc.getById(addedId);
//        assertNotNull(u);
//
//        boolean isSuccess = udc.removeAndReturnSuccess(u);
//        assertTrue(isSuccess);
//        assertNull(udc.getById(addedId));
//    }
}