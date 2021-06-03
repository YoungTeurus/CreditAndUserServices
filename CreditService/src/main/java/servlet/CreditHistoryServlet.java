package servlet;

import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.models.error.ErrorMessage;
import com.github.youngteurus.servletdatabase.servlets.BaseServlet;
import com.google.common.hash.Hashing;
import config.Config;
import modelconnectors.CreditDatabaseConnector;
import modelconnectors.PaymentDatabaseConnector;
import models.Credit;
import models.Payment;
import models.out.CreditAndPayments;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="credits", urlPatterns = "/")
public class CreditHistoryServlet extends BaseServlet {
    private final CreditDatabaseConnector creditRepos = CreditDatabaseConnector.getInstance();
    private final PaymentDatabaseConnector paymentRepos = PaymentDatabaseConnector.getInstance();

    @Override
    protected Object processParameters() {
        String userId = getRequestParameterValue("userId");
        String controlValue = getRequestParameterValue("controlValue");

        Object result;

        try {
            if (userId != null) {
                boolean isOperationIllegal = ! checkIfOperationLegalUsingControlValueAndUserId(controlValue);
                if (isOperationIllegal){
                    result = new ErrorMessage(HttpServletResponse.SC_FORBIDDEN,
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

    private Object getUserCreditHistoryById(String userId) throws SQLException, DataBaseConnectionException {
        int parsedUserId;

        try {
            parsedUserId = Integer.parseInt(userId);
        } catch (NumberFormatException ignored){
            return new ErrorMessage(HttpServletResponse.SC_BAD_REQUEST,
                    "Параметр 'userId' содержал неверные данные. Проверьте правильность данных и повторите запрос.");
        }

        List<Credit> credits = creditRepos.getByUserId(parsedUserId);

        List<CreditAndPayments> creditsAndPayments = new ArrayList<>();

        for(Credit credit : credits){
            long creditId = credit.getId();
            List<Payment> creditPayments = paymentRepos.getByCreditId(creditId);
            CreditAndPayments creditAndPayments = new CreditAndPayments(credit, creditPayments);
            creditsAndPayments.add(creditAndPayments);
        }

        return creditsAndPayments;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        // TODO: реализовать добавление записей о новых пользователях, новых кредитах и платежах в servlet.CreditHistoryServlet через POST-запрос.
    }
}
