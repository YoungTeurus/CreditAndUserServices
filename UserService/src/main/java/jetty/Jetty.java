package jetty;

import config.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.UsersServlet;

public class Jetty {
    public static void start() throws Exception {
        Server server = new Server(Integer.parseInt(Config.getPort()));
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new UsersServlet()), "/user");

        server.start();
        server.join();
    }
}
