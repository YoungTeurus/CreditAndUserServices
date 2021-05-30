package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import services.credits.models.User;
import services.main.config.Config;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.credits.models.Credit;
import services.credits.models.Payment;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class CreditHistoryServletTest {
    // TODO: ПЕРЕД ЗАПУСКОМ ТЕСТОВ ЗАПУСКАТЬ JETTY:RUN.

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
    void getCreditsByUserId(){
        List<Credit> credits = connectAndGetArrayListOfCredits(
                Config.getCreditsURL() + "?controlValue=1&userId=1"
        );

        System.out.println(credits);
    }
}