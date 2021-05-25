package config;

public class DBCredentials {
    private final String host;
    private final String user;
    private final String pass;

    public DBCredentials(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

    public String getHost() {
        return host;
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }
}
