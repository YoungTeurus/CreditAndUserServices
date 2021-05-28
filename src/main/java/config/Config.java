package config;

import java.io.*;
import java.util.Properties;

public class Config {

    private static Config instance;

    private String DBHost;
    private String DBLogin;
    private String DBPass;

    private String usersURL;
    private String creditsURL;

    private Config() {
        if (instance == null) {
            instance = this;
        }
        load();
    }

    private void load() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("src/main/resources/config.properties"));

            this.DBHost = property.getProperty("db.host");
            this.DBLogin = property.getProperty("db.user");
            this.DBPass = property.getProperty("db.password");

            this.usersURL = property.getProperty("users.url");
            this.creditsURL = property.getProperty("credits.url");

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    public static void createInstanceIfNotCreated() {
        if (instance == null) {
            instance = new Config();
        }
    }

    public static String getDBHost() {
        createInstanceIfNotCreated();
        return instance.DBHost;
    }

    public static String getDBLogin() {
        createInstanceIfNotCreated();
        return instance.DBLogin;
    }

    public static String getDBPass() {
        createInstanceIfNotCreated();
        return instance.DBPass;
    }

    public static String getUsersURL() {
        createInstanceIfNotCreated();
        return instance.usersURL;
    }

    public static String getCreditsURL() {
        createInstanceIfNotCreated();
        return instance.creditsURL;
    }

}

