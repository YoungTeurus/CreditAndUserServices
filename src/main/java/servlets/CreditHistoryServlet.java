package servlets;

public class CreditHistoryServlet extends BaseServlet{
    @Override
    protected Object processParameters() {
        String userId = getRequestParameterValue("userId");
        return null;
    }
}
