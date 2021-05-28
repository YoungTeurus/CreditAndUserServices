package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import models.*;
import models.error.*;

@WebServlet(name="get", urlPatterns = "/get")
public class MainServlet extends BaseServlet {
    // TODO: в финальной версии serverURL можно задавать из коммандной строки.
    private final String serverURL = "http://localhost:8080/main";

    @Override
    protected Object processParameters() {
        String passport = getRequestParameterValue("passport");
        if (passport != null) {
            String json = connectAndGet(serverURL + "/user?passport=" + passport);
            User user = new Gson().fromJson(json, User.class);
            if (user == null) {
                return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND, "Пользователя с данными паспортными данными не найдено!");
            } else {
                return user;
            }
        }
        return new ErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Что-то пошло не так...");
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
