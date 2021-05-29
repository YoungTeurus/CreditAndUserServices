package config;

import java.io.*;
import java.util.Properties;

public class Config {

    private static Config instance;

    // TODO: временное решение проблемы с БД в Config:
    private String UserServiceDBHost;
    private String UserServiceDBLogin;
    private String UserServiceDBPass;

    private String CreditServiceDBHost;
    private String CreditServiceDBLogin;
    private String CreditServiceDBPass;

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

            this.UserServiceDBHost = property.getProperty("userService.db.host");
            this.UserServiceDBLogin = property.getProperty("userService.db.user");
            this.UserServiceDBPass = property.getProperty("userService.db.password");

            this.CreditServiceDBHost = property.getProperty("creditService.db.host");
            this.CreditServiceDBLogin = property.getProperty("creditService.db.user");
            this.CreditServiceDBPass = property.getProperty("creditService.db.password");

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

    public static String getUserServiceDBHost() {
        createInstanceIfNotCreated();
        return instance.UserServiceDBHost;
    }

    public static String getUserServiceDBLogin() {
        createInstanceIfNotCreated();
        return instance.UserServiceDBLogin;
    }

    public static String getUserServiceDBPass() {
        createInstanceIfNotCreated();
        return instance.UserServiceDBPass;
    }

    public static String getCreditServiceDBHost() {
        createInstanceIfNotCreated();
        return instance.CreditServiceDBHost;
    }

    public static String getCreditServiceDBLogin() {
        createInstanceIfNotCreated();
        return instance.CreditServiceDBLogin;
    }

    public static String getCreditServiceDBPass() {
        createInstanceIfNotCreated();
        return instance.CreditServiceDBPass;
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

