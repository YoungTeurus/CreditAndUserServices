package servlets;

import com.google.gson.Gson;
import config.Config;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsersServletTest {
    // TODO: ПЕРЕД ЗАПУСКОМ ТЕСТОВ ЗАПУСКАТЬ JETTY:RUN.

    private User connectAndGet(String URL) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        String json = response.readEntity(String.class);
        return new Gson().fromJson(json, User.class);
    }

    @Test
    public void getUserById() {
        User user = connectAndGet(Config.getUsersURL() + "?id=1");
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(LocalDate.of(2021,5,25), user.getBirthDate());
        Assertions.assertEquals("John", user.getFirstname());
        Assertions.assertEquals("Johnstor", user.getSurname());
        Assertions.assertEquals("123456789", user.getDriverLicenceId());
        Assertions.assertEquals("1234543213", user.getPassportNumber());
        Assertions.assertEquals("987654321", user.getTaxPayerID());
        Assertions.assertEquals(2, user.getSex().getId());
    }

    @Test
    public void getUserByPassport() {
        User user = connectAndGet(Config.getUsersURL() + "?passport=1234543213");
        System.out.println(user);
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(LocalDate.of(2021,5,25), user.getBirthDate());
        Assertions.assertEquals("John", user.getFirstname());
        Assertions.assertEquals("Johnstor", user.getSurname());
        Assertions.assertEquals("123456789", user.getDriverLicenceId());
        Assertions.assertEquals("1234543213", user.getPassportNumber());
        Assertions.assertEquals("987654321", user.getTaxPayerID());
        Assertions.assertEquals(2, user.getSex().getId());
    }


}