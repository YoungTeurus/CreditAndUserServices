package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
            if (!new File("MainService.properties").exists()) {
                File file = new File("MainService.properties");
                file.createNewFile();
                try(FileWriter writer = new FileWriter(file)) {
                    writer.write("users.url = \n");
                    writer.write("credits.url = \n");
                    writer.write("main.port = \n");
                    writer.flush();
                }
                throw new RuntimeException("Файла конфигурации не существовало, он был создан. Задайте значения");
            }
            property.load(new FileInputStream("MainService.properties"));

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


