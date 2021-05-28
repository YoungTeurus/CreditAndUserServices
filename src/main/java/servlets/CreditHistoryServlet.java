package servlets;

import javax.servlet.annotation.WebServlet;

@WebServlet(name="credits", urlPatterns = "/credit")
public class CreditHistoryServlet extends BaseServlet{
    @Override
    protected Object processParameters() {
        String userId = getRequestParameterValue("userId");
        return null;
    }
}
