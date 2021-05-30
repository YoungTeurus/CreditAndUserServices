package servlet;

import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.models.error.ErrorMessage;
import com.github.youngteurus.servletdatabase.servlets.BaseServlet;
import com.google.gson.Gson;
import modelconnectors.UserDatabaseConnector;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="users", urlPatterns = "/")
public class UsersServlet extends BaseServlet {
    private final UserDatabaseConnector repos = UserDatabaseConnector.getInstance();

    @Override
    protected Object processParameters() {
        String id = getRequestParameterValue("id");

        String firstname = getRequestParameterValue("firstname");
        String surname = getRequestParameterValue("surname");
        String passportNumber = getRequestParameterValue("passportNumber");

        String getAll = getRequestParameterValue("getAll");

        Object result;
        try {
            if(id != null){
                result = getUserById(id);
            } else if (firstname != null && surname != null && passportNumber != null){
                result = getUserByFirstnameSurnameAndPassport(firstname, surname, passportNumber);
            } else if (getAll != null && getAll.equals("1")) {
                result = getAllUsers();
            } else {
                result = new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                        "Запрос не содержал значимых параметров или был неполным. Проверьте правильность данных и повторите запрос.");
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

    private Object getUserById(String id) throws SQLException, DataBaseConnectionException {
        int parsedId;

        try {
            parsedId = Integer.parseInt(id);
        } catch (NumberFormatException ignored){
            return new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST,
                    "Параметр 'id' содержал неверные данные. Проверьте правильность данных и повторите запрос.");
        }

        User user = repos.getById(parsedId);
        if (user == null) {
            return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                    "Пользователь с данным ID не найден");
        }
        return user;
    }

    private Object getUserByFirstnameSurnameAndPassport(String firstname, String surname, String passportNumber) throws SQLException, DataBaseConnectionException {
        List<User> users = repos.getByFirstnameSurnameAndPassport(firstname, surname, passportNumber);
        if (users.isEmpty()) {
            return new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                    "Пользователь с заданным именем, фамилией и пасспортными данными не найден.");
        }
        // TODO: возможность того, что найденных пользователей окажется больше 1-го всё-ещё сохраняется.
        //  Возможно всё-таки стоит возвращать список всех пользователей, запрашивая у пользователя уточнение по ID.
        return users.get(0);
    }

    private Object getAllUsers() throws SQLException, DataBaseConnectionException {
        List<User> users = repos.getAll();
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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        // TODO: реализовать удаление пользователей.
    }
}
