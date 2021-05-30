package servlets;

import com.github.youngteurus.servletdatabase.models.error.ErrorMessage;
import com.github.youngteurus.servletdatabase.servlets.BaseServlet;
import com.google.gson.Gson;
import config.Config;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Credit;
import models.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="main", urlPatterns = "/")
public class MainServlet extends BaseServlet {

    @Override
    protected Object processParameters() {
        String passport = getRequestParameterValue("passport");
        return handle(passport);
    }

    private Object handle(String passport) {
        if (passport != null) {
            String json = connectAndGet(Config.getUsersURL() + "?passport=" + passport);
            User user = new Gson().fromJson(json, User.class);
            if (user == null) {
                return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND, "Пользователя с данными паспортными данными не найдено!");
            } else {
                return getCreditInfoByUser(user);
            }
        }
        return new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST, "Укажите номер паспорта для поиска.");
    }

    private Object getCreditInfoByUser(User user) {
        Credit credit = new Gson().fromJson(connectAndGet(Config.getCreditsURL() + "?userId=" + user.getId()), Credit.class);
        if (credit == null) {
            return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND, "Записей о кредитах для данного пользователя не нейдено!");
        }
        return credit;
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
