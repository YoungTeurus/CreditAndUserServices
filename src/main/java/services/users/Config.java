package services.users;

import java.io.*;
import java.util.Properties;

public class Config {

    private static Config instance;

    private String UserServiceDBHost;
    private String UserServiceDBLogin;
    private String UserServiceDBPass;

    private Config() {
        if (instance == null) {
            instance = this;
        }
        load();
    }

    private void load() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("src/main/resources/UserService.properties"));

            this.UserServiceDBHost = property.getProperty("userService.db.host");
            this.UserServiceDBLogin = property.getProperty("userService.db.user");
            this.UserServiceDBPass = property.getProperty("userService.db.password");

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

}

