package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import services.main.config.Config;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

import services.users.models.User;
import models.error.ErrorMessage;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class UsersServletTest {
    // TODO: ПЕРЕД ЗАПУСКОМ ТЕСТОВ ЗАПУСКАТЬ JETTY:RUN.

    // TODO: ЛОВИТСЯ ПЛАВАЮЩИЙ БАГ:
    //  ПРИ ПЕРВОМ ЗАПУСКЕ ТЕСТОВ КАЖДЫЙ ЗАПРОС ВОЗВРАЩАЕТ NULL.

    private User connectAndGetSingleUser(String URL) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        String json = response.readEntity(String.class);

        Type userType = new TypeToken<User>(){}.getType();

        return new Gson().fromJson(json, userType);
    }

    private List<User> connectAndGetMultipleUsers(String URL){
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        String json = response.readEntity(String.class);

        Type listType = new TypeToken<ArrayList<User>>(){}.getType();

        return new Gson().fromJson(json, listType);
    }

    private ErrorMessage connectAndGetError(String URL){
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        String json = response.readEntity(String.class);

        Type errorMessageType = new TypeToken<ErrorMessage>(){}.getType();

        return new Gson().fromJson(json, errorMessageType);
    }

    @Test
    public void getUserById() {
        User user = connectAndGetSingleUser(Config.getUsersURL() +
                "?id=1"
        );

        System.out.println(user);

        assertEquals(1, user.getId());
        assertEquals(LocalDate.of(2021,5,30), user.getBirthDate());
        assertEquals("TESTUSER1", user.getFirstname());
        assertEquals("TESTUSER1", user.getSurname());
        assertEquals("QWERTY-12", user.getDriverLicenceId());
        assertEquals("1234 567890", user.getPassportNumber());
        assertEquals("123456789012", user.getTaxPayerID());
        assertEquals(1, user.getSex().getId());
    }

    @Test
    public void getUserByFirstnameSurnameAndPassport() {
        // Пробел замещается символом "%20"
        User user = connectAndGetSingleUser(Config.getUsersURL() +
                "?firstname=TESTUSER1&surname=TESTUSER1&passportNumber=1234%20567890"
        );

        System.out.println(user);

        assertEquals(1, user.getId());
        assertEquals(LocalDate.of(2021,5,30), user.getBirthDate());
        assertEquals("TESTUSER1", user.getFirstname());
        assertEquals("TESTUSER1", user.getSurname());
        assertEquals("QWERTY-12", user.getDriverLicenceId());
        assertEquals("1234 567890", user.getPassportNumber());
        assertEquals("123456789012", user.getTaxPayerID());
        assertEquals(1, user.getSex().getId());
    }

    @Test
    public void getAllUsers(){
        System.out.println(Config.getUsersURL());
        List<User> users = connectAndGetMultipleUsers(Config.getUsersURL() +
                "?getAll=1"
        );
        System.out.println(users);

        assertNotEquals(0, users.size());
    }

    @Test
    public void getErrorWithoutParameters(){
        ErrorMessage errorMessage = connectAndGetError(
                Config.getUsersURL()
        );

        System.out.println(errorMessage);

        assertNotNull(errorMessage);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, errorMessage.getError());
    }

    @Test
    public void getErrorNotEnoughParameters(){
        ErrorMessage errorMessage = connectAndGetError(
                Config.getUsersURL() + "?firstname=TESTUSER1"
        );

        System.out.println(errorMessage);

        assertNotNull(errorMessage);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, errorMessage.getError());
    }

    @Test
    public void getErrorBadId(){
        ErrorMessage errorMessage = connectAndGetError(
                Config.getUsersURL() + "?id=QWERTY"
        );

        System.out.println(errorMessage);

        assertNotNull(errorMessage);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, errorMessage.getError());
    }
}