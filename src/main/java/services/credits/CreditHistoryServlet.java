package services.credits;

import database.DataBaseConnectionException;
import services.credits.modelconnectors.CreditDatabaseConnector;
import services.credits.modelconnectors.PaymentDatabaseConnector;
import services.credits.modelconnectors.UserDatabaseConnector;
import services.credits.models.Credit;
import services.credits.models.User;
import models.error.ErrorMessage;
import servlets.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="credits", urlPatterns = "/credit")
public class CreditHistoryServlet extends BaseServlet {
    private final UserDatabaseConnector usersRepos = UserDatabaseConnector.getInstance();
    private final CreditDatabaseConnector creditRepos = CreditDatabaseConnector.getInstance();
    private final PaymentDatabaseConnector paymentRepos = PaymentDatabaseConnector.getInstance();

    @Override
    protected Object processParameters() {
        String userId = getRequestParameterValue("userId");
        String controlValue = getRequestParameterValue("controlValue");

        Object result;

        try {
            if (userId != null) {
                boolean isOperationIllegal = ! checkIfOperationLegalUsingControlValueAndUserId(userId, controlValue);
                if (isOperationIllegal){
                    result = new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                            "Передано неверное контрольное значение: " + controlValue + ". Проверьте правильность данных и повторите запрос.");
                } else {
                    result = getUserCreditHistoryById(userId);
                }
            } else {
                result = new ErrorMessage(HttpServletResponse.SC_NOT_FOUND,
                        "Запрос не содержал ни одного параметра.");
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

    private boolean checkIfOperationLegalUsingControlValueAndUserId(String userId, String controlValue){
        if (controlValue == null){
            return false;
        }
        // TODO: придумать проверку для контрольного значения.
        return controlValue.equals("1");
    }

    private Object getUserCreditHistoryById(String userId) throws SQLException, DataBaseConnectionException {
        int parsedUserId;

        try {
            parsedUserId = Integer.parseInt(userId);
        } catch (NumberFormatException ignored){
            return new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST,
                    "Параметр 'userId' содержал неверные данные. Проверьте правильность данных и повторите запрос.");
        }

        List<Credit> credits = creditRepos.getByUserId(parsedUserId);

        // TODO: придумать, как возвращать credits + payments в одном объекте

        return credits;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        // TODO: реализовать добавление записей о новых пользователях, новых кредитах и платежах в CreditHistoryServlet через POST-запрос.
    }
}
