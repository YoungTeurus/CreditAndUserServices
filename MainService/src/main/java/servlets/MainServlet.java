package servlets;

import com.github.youngteurus.servletdatabase.models.error.ErrorMessage;
import com.github.youngteurus.servletdatabase.servlets.BaseServlet;
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
import models.out.UserCredit;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="main", urlPatterns = "/")
public class MainServlet extends BaseServlet {

    @Override
    protected Object processParameters() {
        String firstname = getRequestParameterValue("firstname");
        String surname = getRequestParameterValue("surname");
        String passport = getRequestParameterValue("passportNumber");
        String driverID = getRequestParameterValue("driverID");
        String taxID = getRequestParameterValue("taxID");
        return handle(firstname, surname, passport, driverID, taxID);
    }

    private Object handle(String firstname, String surname, String passport, String driverID, String taxID) {
        List<Object> result = new ArrayList<>();
        if (isFullNameCorrect(firstname, surname)) {
            User user = getUserByIds(firstname, surname, passport ,driverID, taxID);
            result.add(user);
            if (user != null) {
                System.out.println(user);
                List<Credit> credit = getCreditInfoByUser(user);
                System.out.println(credit);
                if (credit == null) {
                    result.add(new ErrorMessage(HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден в сервисе кредитных историй."));
                } else {
                    return new UserCredit(user, credit);
                }
                return result;
            } else {
                return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND, "Пользователь с данными индификационными данными не найден.");
            }
        } else {
            return new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST, "Не указано ФИО.");
        }
    }

    private User getUserByIds(String firstname, String surname, String passport, String driverID, String taxID) {
        User user = null;
        if (passport != null) {
            user = findByFullNameAndPassport(firstname,surname,passport);
        }
        if (user == null) {
            if (driverID != null) {
                user = findByFullNameAndDriverId(firstname,surname,driverID);
            }
        }
        if (user == null) {
            user = findByFullNameAndTaxID(firstname,surname,taxID);
        }
        return user;
    }

    private User findByFullNameAndPassport(String firstname, String surname, String passport) {
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname + "&passportNumber=" + passport);
        return new Gson().fromJson(json, User.class);
    }

    private User findByFullNameAndDriverId(String firstname, String surname, String driverID) {
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname + "&driverID=" + driverID);
        return new Gson().fromJson(json, User.class);
    }

    private User findByFullNameAndTaxID(String firstname, String surname, String taxID) {
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname + "&taxID=" + taxID);
        return new Gson().fromJson(json, User.class);
    }

    private boolean isFullNameCorrect(String firstname, String surname) {
        if (firstname == null) {
            return false;
        } else if (surname == null) {
            return false;
        } else {
            return true;
        }
    }

    private List<Credit> getCreditInfoByUser(User user) {
        Type listType = new TypeToken<ArrayList<Credit>>(){}.getType();
        return new Gson().fromJson(connectAndGet(Config.getCreditsURL() + "?userId=" + user.getCreditServiceId() + "&controlValue=1"), listType);
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
