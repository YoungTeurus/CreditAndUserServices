package config;

import java.io.*;
import java.util.Properties;

public class Config {

    public static DBCredentials getCredentials() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("src/main/resources/config.properties"));

            String host = property.getProperty("db.host");
            String user = property.getProperty("db.user");
            String password = property.getProperty("db.password");

            return new DBCredentials(host, user, password);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        return null;
    }

}

