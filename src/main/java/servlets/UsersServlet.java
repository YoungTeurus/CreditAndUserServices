package servlets;

import database.DataBaseConnectionException;
import modelconnectors.UserDatabaseConnector;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import models.error.*;

import models.User;

@WebServlet(name="users", urlPatterns = "/user")
public class UsersServlet extends HttpServlet implements Servlet {
    private final UserDatabaseConnector repos = UserDatabaseConnector.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        try {
            if (id == null) {
                sendError(new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST,
                        "Вы не указали id пользователя в запросе"), resp);
                return;
            }
            try {
                User user = repos.get(Integer.parseInt(id));
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
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = new Gson().fromJson(request.getReader(), User.class);
        try {
            repos.addAndReturnId(user);
            sendError(new ErrorMessage(HttpServletResponse.SC_OK,
                    "Пользователь успешно добавлен"), response);
        } catch (SQLException e) {
            sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Ошибка в SQL запросе"), response);
        } catch (DataBaseConnectionException e) {
            sendError(new ErrorMessage(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Не удалось подключиться к базе данных"), response);
        }
    }
}
