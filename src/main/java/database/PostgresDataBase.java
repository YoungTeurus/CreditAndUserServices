package database;

import services.users.Config;

import java.sql.*;

public class PostgresDataBase implements DataBase {
    private static PostgresDataBase instance;
    private static PostgresDataBase userServiceInstance;
    private static PostgresDataBase creditServiceInstance;

    private final String dbURL;
    private final String user;
    private final String pass;


    public PostgresDataBase(String dbURL, String user, String pass) {
        this.dbURL = dbURL;
        this.user = user;
        this.pass = pass;
        if (instance == null) {
            instance = this;
        }
    }

    static Connection connectionInstance;

    private void createConnectionInstance() throws DataBaseConnectionException{
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DataBaseConnectionException("PostgreSQL JDBC Driver is not found");
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        connectionInstance = null;

        try {
            connectionInstance = DriverManager
                    .getConnection(dbURL, user, pass);

        } catch (SQLException e) {
            throw new DataBaseConnectionException(String.format("Connection Failed. dbURL = %s, user = %s, pass = %s", dbURL, user, pass));
        }
    }

    public ResultSet executeStatement(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
    }

    public Connection getConnection() throws DataBaseConnectionException {
        if (connectionInstance == null) {
            createConnectionInstance();
        }
        return connectionInstance;
    }

    // TODO: либо изменить класс Config для разных сервисов, либо отказаться от getInstance.
    public static PostgresDataBase getInstance() {
        if (instance == null) {
            instance = new PostgresDataBase(Config.getUserServiceDBHost(), Config.getUserServiceDBLogin(), Config.getUserServiceDBPass());
        }
        return instance;
    }

    // TODO: временное решение для разных сервисов:
    public static PostgresDataBase getUserServiceInstance() {
        if (userServiceInstance == null) {
            userServiceInstance = new PostgresDataBase(
                    Config.getUserServiceDBHost(),
                    Config.getUserServiceDBLogin(),
                    Config.getUserServiceDBPass()
            );
        }
        return userServiceInstance;
    }

    public static PostgresDataBase getCreditServiceInstance() {
        if (creditServiceInstance == null) {
            creditServiceInstance = new PostgresDataBase(
                    Config.getCreditServiceDBHost(),
                    Config.getCreditServiceDBLogin(),
                    Config.getCreditServiceDBPass()
            );
        }
        return creditServiceInstance;
    }
}
