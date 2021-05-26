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

@WebServlet(name="users", urlPatterns = "/user")
public class UsersServlet extends HttpServlet implements Servlet {
    private final UserDatabaseConnector repos = UserDatabaseConnector.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String remove = req.getParameter("remove");
        try {
            if (id == null) {
                sendObject(repos.getAll(), resp);
                return;
            }
            try {
                if (remove != null && remove.equals("true")) {
                    boolean result = repos.removeAndReturnSuccess(Long.parseLong(id));
                    if (result) {
                        sendError(new ErrorMessage(HttpServletResponse.SC_OK,
                                "Пользователь с id = " + id + " успешно удален."), resp);
                    } else {
                        sendError(new ErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                "Произошла ошибка при удалении"), resp);
                    }
                    return;
                }
                User user = repos.get(Integer.parseInt(id));
                if (user == null) {
                    sendError(new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                            "Пользователь с данным ID не найден"), resp);
                    return;
                }
                sendObject(user, resp);
            } catch (DataBaseConnectionException e) {
                sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                        "Не удалось подключиться к базе данных"), resp);
            } catch (SQLException e) {
                sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                        "Ошибка в SQL запросе"), resp);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException throwables) {
            sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Ошибка в SQL запросе"), resp);
        } catch (DataBaseConnectionException e) {
            sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к базе данных"), resp);
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
