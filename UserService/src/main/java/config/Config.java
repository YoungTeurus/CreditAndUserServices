package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config instance;

    private String UserServiceDBHost;
    private String UserServiceDBLogin;
    private String UserServiceDBPass;
    private String port;

    private Config() {
        if (instance == null) {
            instance = this;
        }
        load();
    }

    private void load() {
        Properties property = new Properties();
        try {
            // Для тестов, иначе не находит
            if (!new File("UserService/src/main/resources/UserService.properties").exists()) {
                property.load(new FileInputStream("../UserService/src/main/resources/UserService.properties"));
            } else {
                property.load(new FileInputStream("UserService/src/main/resources/UserService.properties"));
            }

            this.UserServiceDBHost = property.getProperty("userService.db.host");
            this.UserServiceDBLogin = property.getProperty("userService.db.user");
            this.UserServiceDBPass = property.getProperty("userService.db.password");
            this.port = property.getProperty("userService.port");

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует! (Users)");
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

    public static String getPort() {
        createInstanceIfNotCreated();
        return instance.port;
    }

}

