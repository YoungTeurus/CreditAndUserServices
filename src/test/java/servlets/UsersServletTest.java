package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.Config;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class UsersServletTest {
    // TODO: ПЕРЕД ЗАПУСКОМ ТЕСТОВ ЗАПУСКАТЬ JETTY:RUN.

    private User connectAndGetSingleUser(String URL) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        String json = response.readEntity(String.class);
        return new Gson().fromJson(json, User.class);
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

    @Test
    public void getUserById() {
        User user = connectAndGetSingleUser(Config.getUsersURL() + "?id=1");
        assertEquals(1, user.getId());
        assertEquals(LocalDate.of(2021,5,25), user.getBirthDate());
        assertEquals("John", user.getFirstname());
        assertEquals("Johnstor", user.getSurname());
        assertEquals("123456789", user.getDriverLicenceId());
        assertEquals("1234543213", user.getPassportNumber());
        assertEquals("987654321", user.getTaxPayerID());
        assertEquals(2, user.getSex().getId());
    }

    @Test
    public void getUserByPassport() {
        User user = connectAndGetSingleUser(Config.getUsersURL() + "?passport=1234543213");
        System.out.println(user);
        assertEquals(1, user.getId());
        assertEquals(LocalDate.of(2021,5,25), user.getBirthDate());
        assertEquals("John", user.getFirstname());
        assertEquals("Johnstor", user.getSurname());
        assertEquals("123456789", user.getDriverLicenceId());
        assertEquals("1234543213", user.getPassportNumber());
        assertEquals("987654321", user.getTaxPayerID());
        assertEquals(2, user.getSex().getId());
    }

    @Test
    public void getUserByFirstname(){
        List<User> users = connectAndGetMultipleUsers(Config.getUsersURL() + "?firstname=Adam");
        System.out.println(users);

        assertEquals(3, users.size());
    }
}