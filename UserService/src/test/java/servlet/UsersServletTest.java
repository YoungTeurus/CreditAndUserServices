package servlet;

import com.github.youngteurus.servletdatabase.models.error.ErrorMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.User;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UsersServletTest {
    // TODO: ПЕРЕД ЗАПУСКОМ ТЕСТОВ ЗАПУСКАТЬ JETTY:RUN.

    // TODO: ЛОВИТСЯ ПЛАВАЮЩИЙ БАГ:
    //  ПРИ ПЕРВОМ ЗАПУСКЕ ТЕСТОВ КАЖДЫЙ ЗАПРОС ВОЗВРАЩАЕТ NULL.

    private String serviceURL = "http://localhost:8081/user";

    private User GETAndGetSingleUser(String URL) {
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

    private List<User> GETAndGetMultipleUsers(String URL){
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

    private ErrorMessage GETAndGetError(String URL){
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

    private ErrorMessage POSTAndGetError(Object object, String URL){
        String jsonObject = new Gson().toJson(object);

        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);

        // TODO: Здесь же мы отправляем POST-запрос?
        // Было:
        // Response response = request.get();
        Response response = request.post(Entity.json(jsonObject));


        response.bufferEntity();
        String json = response.readEntity(String.class);

        Type errorMessageType = new TypeToken<ErrorMessage>(){}.getType();

        return new Gson().fromJson(json, errorMessageType);
    }

    @Test
    public void getUserById() {
        User user = GETAndGetSingleUser(serviceURL +
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
        User user = GETAndGetSingleUser(serviceURL +
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
        List<User> users = GETAndGetMultipleUsers(
                serviceURL + "?getAll=1"
        );
        System.out.println(users);

        assertNotEquals(0, users.size());
    }

    @Test
    public void getErrorWithoutParameters(){
        ErrorMessage errorMessage = GETAndGetError(
                serviceURL
        );

        System.out.println(errorMessage);

        assertNotNull(errorMessage);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, errorMessage.getError());
    }

    @Test
    public void getErrorNotEnoughParameters(){
        ErrorMessage errorMessage = GETAndGetError(
                serviceURL + "?firstname=TESTUSER1"
        );

        System.out.println(errorMessage);

        assertNotNull(errorMessage);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, errorMessage.getError());
    }

    @Test
    public void getErrorBadId(){
        ErrorMessage errorMessage = GETAndGetError(
                "http://localhost:8080/user?id=QWERTY"
        );

        System.out.println(errorMessage);

        assertNotNull(errorMessage);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, errorMessage.getError());
    }

    @Test
    public void addUser(){
        final Random random = new Random();
        final User testUser = new User.Builder("TEST_USER")
                .driverLicenceId(String.valueOf(random.nextInt()))
                .creditServiceId(random.nextLong())
                .build();

        ErrorMessage errorMessage = POSTAndGetError(
                testUser,
                serviceURL
        );

        System.out.println(errorMessage);
        assertEquals(HttpServletResponse.SC_OK, errorMessage.getError());
    }
}