package modelconnector;

import database.DataBaseConnectionException;
import database.constructor.LongParameter;
import database.constructor.Parameter;
import database.constructor.StringParameter;
import modelconnectors.UserDatabaseConnector;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserDatabaseConnectorTest {
    private final Random random = new Random();
    private final User testUser = new User.Builder("TEST_USER").driverLicenceId(String.valueOf(random.nextInt())).build();

    private final UserDatabaseConnector udb = UserDatabaseConnector.getInstance();

    @Test
    void getById() throws SQLException, DataBaseConnectionException {
        User users = udb.getById(1);
        System.out.println(users);
    }

    @Test
    void getAll() throws SQLException, DataBaseConnectionException {
        List<User> users = udb.getAll();
        System.out.println(users);
    }

//    @Test
//    void add() throws SQLException, DataBaseConnectionException {
//        String addedDriverLicenceId = testUser.getDriverLicenceId();
//
//        long addedId = udb.addAndReturnId(testUser);
//        System.out.println("Added user with ID =" + addedId);
//
//        User addedUser = udb.getById(addedId);
//        assertNotNull(addedUser);
//        System.out.println("addedUser: " + addedUser);
//        Assertions.assertEquals(addedDriverLicenceId, addedUser.getDriverLicenceId());
//    }

    @Test
    void getByParameters(){
        List<User> users = getByFirstname("TESTUSER1");
        System.out.println("Найдено " + users.size() + " TESTUSER1-ов.");
        System.out.println(users);

        users = getBySurname("TESTUSER2");
        System.out.println("Найдено " + users.size() + " TESTUSER2-ей.");
        System.out.println(users);

        users = getByFirstnameAndSurnameAndId("TESTUSER1", "TESTUSER1", 1);
        System.out.println("Найдено " + users.size() + " TESTUSER1-ов TESTUSER1-ей c id=1.");
        System.out.println(users);
    }

    private List<User> getByFirstname(String firstname){
        // Составляю список параметров, по которым делать выборку:
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));

        List<User> users;

        try {
            // Получаю список пользователей по параметру:
            users = udb.getByParameters(params);

        } catch (SQLException | DataBaseConnectionException e) {
            e.printStackTrace();
            users = Collections.emptyList();
        }

        return users;
    }

    private List<User> getBySurname(String surname){
        // Составляю список параметров, по которым делать выборку:
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("surname", surname));

        List<User> users;

        try {
            // Получаю список пользователей по параметру:
            users = udb.getByParameters(params);

        } catch (SQLException | DataBaseConnectionException e) {
            e.printStackTrace();
            users = Collections.emptyList();
        }

        return users;
    }

    private List<User> getByFirstnameAndSurnameAndId(String firstname, String surname, long id){
// Составляю список параметров, по которым делать выборку:
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));
        params.add(new StringParameter("surname", surname));
        params.add(new LongParameter("id", id));

        List<User> users;

        try {
            // Получаю список пользователей по параметру:
            users = udb.getByParameters(params);

        } catch (SQLException | DataBaseConnectionException e) {
            e.printStackTrace();
            users = Collections.emptyList();
        }

        return users;
    }
}