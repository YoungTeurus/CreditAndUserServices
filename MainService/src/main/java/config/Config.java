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

    private String securePhrase;
    private String usersSecurePhrase;
    private String creditsSecurePhrase;

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
                    writer.write("users.securePhrase = \n");
                    writer.write("credits.securePhrase = \n");
                    writer.write("main.securePhrase = \n");
                    writer.flush();
                }
                throw new RuntimeException("Файла конфигурации не существовало, он был создан. Задайте значения");
            }
            property.load(new FileInputStream("MainService.properties"));

            this.usersURL = property.getProperty("users.url");
            this.creditsURL = property.getProperty("credits.url");
            this.port = property.getProperty("main.port");

            this.securePhrase = property.getProperty("main.securePhrase");
            this.usersSecurePhrase = property.getProperty("users.securePhrase");
            this.creditsSecurePhrase = property.getProperty("credits.securePhrase");

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

    public static String getSecurePhrase() {
        createInstanceIfNotCreated();
        return instance.securePhrase;
    }

    public static String getUsersSecurePhrase() {
        createInstanceIfNotCreated();
        return instance.usersSecurePhrase;
    }

    public static String getCreditsSecurePhrase() {
        createInstanceIfNotCreated();
        return instance.creditsSecurePhrase;
    }
}


