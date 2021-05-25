package servlets;

import database.DataBaseConnectionException;
import database.PostgresDataBase;
import models.User;
import modelconnectors.UserDatabaseConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import config.*;

@WebServlet(name="database", urlPatterns = "/db")
public class DataBaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBCredentials credentials = Config.getCredentials();

        assert credentials != null;

        User u = new User.Builder("John").build();

        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();

        try {
            UserDatabaseConnector.getInstance().addAndReturnId(u);
        } catch (DataBaseConnectionException e){
            printWriter.write("Произошла ошибка при подключении к БД!\n");
        } catch (SQLException e) {
            printWriter.write("Произошла ошибка при добавлении пользователя в БД!\n");
        }

        printWriter.write("Обычное выполнение.");
        printWriter.close();
    }
}
