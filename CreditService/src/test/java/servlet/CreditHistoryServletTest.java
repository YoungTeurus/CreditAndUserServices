package servlet;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.Config;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jetty.Jetty;
import models.Credit;
import models.Payment;
import models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditHistoryServletTest {

    @BeforeAll
    static void init()  {
        (new Thread(() -> {
            try {
                Jetty.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
    }

    private String serviceURL = "http://localhost:8080/credit";

    private String calculateControlValue() {
        String code = Config.getSecurePhrase();
        String value = code + LocalDate.now();
        String encrypted = Hashing.sha256()
                .hashString(value, StandardCharsets.UTF_8)
                .toString();
        return encrypted;
    }

    private User connectAndGetUser(String URL){
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

    private List<Credit> connectAndGetArrayListOfCredits(String URL) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        String json = response.readEntity(String.class);

        Type listType = new TypeToken<ArrayList<Credit>>(){}.getType();

        return new Gson().fromJson(json, listType);
    }

    private List<Payment> connectAndGetArrayListOfPayments(String URL) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(URL);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        response.bufferEntity();
        String json = response.readEntity(String.class);

        Type listType = new TypeToken<ArrayList<Payment>>(){}.getType();

        return new Gson().fromJson(json, listType);
    }

    @Test
    void getCreditsByUserId() {
        List<Credit> credits = connectAndGetArrayListOfCredits(
                serviceURL + "?userId=1" + "&controlValue=" + calculateControlValue()
        );

        System.out.println(credits);
        assertNotEquals(0, credits.size());
    }
}