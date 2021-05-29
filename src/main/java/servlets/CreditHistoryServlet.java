package servlets;

import database.DataBaseConnectionException;
import modelconnectors.CreditDatabaseConnector;
import modelconnectors.PaymentDatabaseConnector;
import models.Credit;
import models.Payment;
import models.error.ErrorMessage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="credits", urlPatterns = "/credit")
public class CreditHistoryServlet extends BaseServlet{
    private final CreditDatabaseConnector creditRepos = CreditDatabaseConnector.getInstance();
    private final PaymentDatabaseConnector paymentRepos = PaymentDatabaseConnector.getInstance();

    @Override
    protected Object processParameters() {
        String userId = getRequestParameterValue("userId");
        String creditId = getRequestParameterValue("creditId");

        Object result;

        try {
            if (userId != null) {
                result = handleUserId(userId);
            } else if (creditId != null){
                result = handleCreditId(creditId);
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

    private Object handleUserId(String userId) throws SQLException, DataBaseConnectionException {
        int _userId = Integer.parseInt(userId);

        List<Credit> creditsList = creditRepos.getByUserId(_userId);

        return creditsList;
    }

    private Object handleCreditId(String creditId) throws SQLException, DataBaseConnectionException {
        int _creditId = Integer.parseInt(creditId);

        List<Payment> paymentsList = paymentRepos.getByCreditId(_creditId);

        return paymentsList;
    }
}
