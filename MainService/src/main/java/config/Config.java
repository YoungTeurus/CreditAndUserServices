package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config instance;

    private String usersURL;
    private String creditsURL;
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
            if (!new File("MainService/src/main/resources/MainService.properties").exists()) {
                property.load(new FileInputStream("../MainService/src/main/resources/MainService.properties"));
            } else {
                property.load(new FileInputStream("MainService/src/main/resources/MainService.properties"));
            }

            this.usersURL = property.getProperty("users.url");
            this.creditsURL = property.getProperty("credits.url");
            this.port = property.getProperty("main.port");

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует! (Main)");
        }
    }

    public static void createInstanceIfNotCreated() {
        if (instance == null) {
            instance = new Config();
        }
    }

    public static String getCreditsURL() {
        createInstanceIfNotCreated();
        return instance.creditsURL;
    }

    public static String getUsersURL() {
        createInstanceIfNotCreated();
        return instance.usersURL;
    }

    public static String getPort() {
        createInstanceIfNotCreated();
        return instance.port;
    }
}


