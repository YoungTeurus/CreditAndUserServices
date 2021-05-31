package servlets;

import com.github.youngteurus.servletdatabase.models.error.ErrorMessage;
import com.github.youngteurus.servletdatabase.servlets.BaseServlet;
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
import models.Credit;
import models.User;
import models.out.UserCredit;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="main", urlPatterns = "/")
public class MainServlet extends BaseServlet {

    @Override
    protected Object processParameters() {
        String firstname = getRequestParameterValue("firstname");
        String surname = getRequestParameterValue("surname");
        String patronymic = getRequestParameterValue("patronymic");
        String passport = getRequestParameterValue("passportNumber");
        String driverID = getRequestParameterValue("driverID");
        String taxID = getRequestParameterValue("taxID");
        String controlValue = getRequestParameterValue("controlValue");
        return handle(firstname, surname, patronymic, passport, driverID, taxID, controlValue);
    }

    private Object handle(String firstname, String surname, String patronymic, String passport, String driverID, String taxID, String controlValue) {
        boolean isOperationIllegal = ! checkIfOperationLegalUsingControlValueAndUserId(controlValue);
        if (isOperationIllegal) {
            return new ErrorMessage(HttpServletResponse.SC_FORBIDDEN,
                    "Передано неверное контрольное значение: " + controlValue + ". Проверьте правильность данных и повторите запрос.");
        }
        List<Object> result = new ArrayList<>();
        if (isFullNameCorrect(firstname, surname, patronymic)) {
            User user = getUserByIds(firstname, surname,patronymic, passport ,driverID, taxID);
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

    private boolean checkIfOperationLegalUsingControlValueAndUserId(String controlValue){
        if (controlValue == null){
            return false;
        }
        String code = Config.getSecurePhrase() + LocalDate.now();
        String encrypted = Hashing.sha256()
                .hashString(code, StandardCharsets.UTF_8)
                .toString();
        return controlValue.equals(encrypted);
    }

    private User getUserByIds(String firstname, String surname, String patronymic, String passport, String driverID, String taxID) {
        User user = null;
        if (passport != null) {
            user = findByFullNameAndPassport(firstname,surname,patronymic,passport);
        }
        if (user == null) {
            if (driverID != null) {
                user = findByFullNameAndDriverId(firstname,surname,patronymic,driverID);
            }
        }
        if (user == null) {
            user = findByFullNameAndTaxId(firstname,surname,patronymic,taxID);
        }
        return user;
    }

    private User findByFullNameAndPassport(String firstname, String surname, String patronymic, String passport) {
        String controlValue = calculateControlValue(Config.getUsersSecurePhrase());
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname
                + "&patronymic=" + patronymic + "&passportNumber=" + passport + "&controlValue=" + controlValue);
        return new Gson().fromJson(json, User.class);
    }

    private User findByFullNameAndDriverId(String firstname, String surname, String patronymic, String driverID) {
        String controlValue = calculateControlValue(Config.getUsersSecurePhrase());
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname
                + "&patronymic=" + patronymic + "&driverID=" + driverID + "&controlValue=" + controlValue);
        return new Gson().fromJson(json, User.class);
    }

    private User findByFullNameAndTaxId(String firstname, String surname, String patronymic, String taxID) {
        String controlValue = calculateControlValue(Config.getUsersSecurePhrase());
        String json = connectAndGet(Config.getUsersURL() + "?firstname=" + firstname + "&surname=" + surname
                + "&patronymic=" + patronymic + "&taxID=" + taxID + "&controlValue=" + controlValue);
        return new Gson().fromJson(json, User.class);
    }

    private String calculateControlValue(String code) {
        String value = code + LocalDate.now();
        String encrypted = Hashing.sha256()
                .hashString(code, StandardCharsets.UTF_8)
                .toString();
        return encrypted;
    }

    private boolean isFullNameCorrect(String firstname, String surname, String patronymic) {
        return firstname != null && surname != null && patronymic != null;
    }

    private List<Credit> getCreditInfoByUser(User user) {
        String controlValue = calculateControlValue(Config.getCreditsSecurePhrase());
        Type listType = new TypeToken<ArrayList<Credit>>(){}.getType();
        return new Gson().fromJson(connectAndGet(Config.getCreditsURL() + "?userId=" + user.getCreditServiceId() + "&controlValue=" + controlValue), listType);
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
