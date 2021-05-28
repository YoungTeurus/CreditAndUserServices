package servlets;

import models.error.ErrorMessage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet implements Servlet {
    private Map<String, String[]> requestParameters;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getRequestParameters(req);
        Object answer = processParameters();
        sendAnswer(answer, resp);
    }

    protected abstract Object processParameters();

    private void sendAnswer(Object answer, HttpServletResponse resp) {
        try {
            // TODO: что делать, если попытались отправить null?
            if (answer == null){
                throw new NotImplementedException();
            }
            // TODO: жуткая проверка, потом убрать.
            //  Засунуть проверку на тип в sendObject?
            if (answer instanceof ErrorMessage){
                sendError((ErrorMessage) answer, resp);
            } else {
                sendObject(answer, resp);
            }
        } catch (IOException ignored){

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getRequestParameters(req);
    }

    private void getRequestParameters(HttpServletRequest req){
        requestParameters = req.getParameterMap();
    }

    protected final String getRequestParameterValue(String name, String defaultValue){
        String returnValue = getRequestParameterValue(name);
        if (returnValue == null){
            return defaultValue;
        }
        return returnValue;
    }

    protected final String getRequestParameterValue(String name){
        String[] valuesArray = getRequestParameterValuesArray(name);
        if(valuesArray == null){
            return null;
        }
        return valuesArray[0];
    }

    protected final String[] getRequestParameterValuesArray(String name){
        if (requestParameters == null){
            throw new RuntimeException("UsersServlet::getRequestParameterValuesArray: requestParameters не инициализирован. Вызовите метод getRequestParameters перед получением параметра!");
        }

        @SuppressWarnings("UnnecessaryLocalVariable")
        String[] valuesArray = requestParameters.get(name);

        return valuesArray;
    }
}
