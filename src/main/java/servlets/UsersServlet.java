package servlets;

import com.google.gson.Gson;
import database.DataBaseConnectionException;
import modelconnectors.UserDatabaseConnector;
import models.User;
import models.error.ErrorMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="users", urlPatterns = "/user")
public class UsersServlet extends BaseServlet {
    private final UserDatabaseConnector repos = UserDatabaseConnector.getInstance();

    @Override
    protected Object processParameters() {
        String id = getRequestParameterValue("id");
        String remove = getRequestParameterValue("remove");
        String passport = getRequestParameterValue("passport");
        String firstname = getRequestParameterValue("firstname");

        Object result;
        try {
            if (remove != null && remove.equals("true")) {
                result = handleRemove(id);
            } else if (passport != null) {
                result = handlePassport(passport);
            } else if (id != null) {
                result = handleId(id);
            } else {
                result = handleFirstname(firstname);
            }
        } catch (SQLException throwables) {
            result = new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Ошибка в SQL запросе");
        } catch (DataBaseConnectionException e) {
            result = new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к базе данных");
        }
        return result;
    }

    private Object handleId(String id) throws SQLException, DataBaseConnectionException {
        if (id == null) {
            return repos.getAll();
        }

        User user = repos.getById(Integer.parseInt(id));
        if (user == null) {
            return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                    "Пользователь с данным ID не найден");
        }
        return user;
    }

    private Object handleRemove(String id) throws SQLException, DataBaseConnectionException {
        boolean result = repos.removeAndReturnSuccess(Long.parseLong(id));
        if (!result) {
            return new ErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Произошла ошибка при удалении пользователя");
        }
        return new ErrorMessage(HttpServletResponse.SC_OK,
                "Пользователь с id = " + id + " успешно удален.");
    }

    private Object handlePassport(String passport) throws SQLException, DataBaseConnectionException {
        User user = repos.getByPassportID(passport);
        if (user == null) {
            return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                    "Пользователь с данными паспортными данными не найден.");
        }
        return user;
    }

    private Object handleFirstname(String firstname) throws SQLException, DataBaseConnectionException{
        List<User> users = repos.getByFirstname(firstname);
        if (users.isEmpty()) {
            return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                    "Пользователи с заданным именем не найдены.");
        }
        return users;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = new Gson().fromJson(request.getReader(), User.class);
        try {
            long id = repos.addAndReturnId(user);
            sendError(new ErrorMessage(HttpServletResponse.SC_OK,
                    "Пользователь успешно добавлен с id = " + id), response);
        } catch (SQLException e) {
            sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Ошибка в SQL запросе"), response);
        } catch (DataBaseConnectionException e) {
            sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к базе данных"), response);
        }
    }
}
