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
import models.Credit;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainServletTest {
    @Test
    public void getUserAndCreditsByFullNameAndPassport() {
        User user = findByFullNameAndPassport("TESTUSER1", "TESTUSER1", "1234567890");
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getId());
        List<Credit> creditList = getCreditInfoByUser(user);
        for (Credit credit : creditList) {
            Assertions.assertEquals(1, credit.getUserId());
        }
    }

    @Test
    public void getUserAndCreditsByFullNameAndDriverId() {
        User user = findByFullNameAndDriverId("TESTUSER1", "TESTUSER1", "QWERTY-12");
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getId());
        List<Credit> creditList = getCreditInfoByUser(user);
        for (Credit credit : creditList) {
            Assertions.assertEquals(1, credit.getUserId());
        }
    }

    @Test
    public void getUserAndCreditsByFullNameAndTaxId() {
        User user = findByFullNameAndTaxId("TESTUSER1", "TESTUSER1", "123456789012");
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getId());
        List<Credit> creditList = getCreditInfoByUser(user);
        for (Credit credit : creditList) {
            Assertions.assertEquals(1, credit.getUserId());
        }
    }

    private List<Credit> getCreditInfoByUser(User user) {
        Type listType = new TypeToken<ArrayList<Credit>>(){}.getType();
        return new Gson().fromJson(connectAndGet(Config.getCreditsURL() + "?userId=" + user.getCreditServiceId() + "&controlValue=1"), listType);
    }

    private User findByFullNameAndPassport(String firstname, String surname, String passport) {
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname + "&passportNumber=" + passport);
        return new Gson().fromJson(json, User.class);
    }

    private User findByFullNameAndDriverId(String firstname, String surname, String driverID) {
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname + "&driverID=" + driverID);
        return new Gson().fromJson(json, User.class);
    }

    private User findByFullNameAndTaxId(String firstname, String surname, String taxID) {
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname + "&taxID=" + taxID);
        return new Gson().fromJson(json, User.class);
    }

    private String connectAndGet(String path) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(path);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        return response.readEntity(String.class);
    }
}