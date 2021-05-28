package servlets;

import com.google.gson.Gson;
import database.DataBaseConnectionException;
import modelconnectors.UserDatabaseConnector;
import models.User;
import models.error.ErrorMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name="users", urlPatterns = "/user")
public class UsersServlet extends BaseServlet {
    private final UserDatabaseConnector repos = UserDatabaseConnector.getInstance();

    @Override
    protected Object processParameters() {
        String id = getRequestParameterValue("id");
        String remove = getRequestParameterValue("remove");
        String passport = getRequestParameterValue("passport");
        Object result;
        if (remove != null && remove.equals("true")) {
            result = handleRemove(id);
        } else if (passport != null) {
            result = handlePassport(passport);
        } else {
            result = handleId(id);
        }
        return result;
    }

    private Object handleId(String id) {
        try {
            if (id == null) {
                return repos.getAll();
            } else {
                User user = repos.get(Integer.parseInt(id));
                if (user == null) {
                    return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                            "Пользователь с данным ID не найден");
                }
                return user;
            }
        } catch (SQLException throwables) {
            return new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Ошибка в SQL запросе");
        } catch (DataBaseConnectionException e) {
            return new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к базе данных");
        }
    }

    private Object handleRemove(String id) {
        try {
            boolean result = repos.removeAndReturnSuccess(Long.parseLong(id));
            if (result) {
                return new ErrorMessage(HttpServletResponse.SC_OK,
                        "Пользователь с id = " + id + " успешно удален.");
            } else {
                return new ErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Произошла ошибка при удалении пользователя");
            }
        } catch (SQLException throwables) {
            return new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Ошибка в SQL запросе");
        } catch (DataBaseConnectionException e) {
            return new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к базе данных");
        }
    }

    private Object handlePassport(String passport) {
        try {
            User user = repos.getByPassportID(passport);
            if (user == null) {
                return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                        "Пользователь с данными паспортными данными не найден.");
            } else {
                return user;
            }
        } catch (SQLException throwables) {
            return new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Ошибка в SQL запросе");
        } catch (DataBaseConnectionException e) {
            return new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к базе данных");
        }
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
