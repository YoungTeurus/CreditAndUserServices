package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config instance;

    private String CreditServiceDBHost;
    private String CreditServiceDBLogin;
    private String CreditServiceDBPass;
    private String port;

    private String securePhrase;

    private Config() {
        if (instance == null) {
            instance = this;
        }
        load();
    }

    private void load() {
        Properties property = new Properties();
        try {
            if (!new File("CreditService.properties").exists()) {
                File file = new File("CreditService.properties");
                file.createNewFile();
                try(FileWriter writer = new FileWriter(file)) {
                    writer.write("creditService.db.host = \n");
                    writer.write("creditService.db.user = \n");
                    writer.write("creditService.db.password = \n");
                    writer.write("creditService.port = \n");
                    writer.write("creditService.securePhrase = \n");
                    writer.flush();
                }
                throw new RuntimeException("Файла конфигурации не существовало, он был создан. Задайте значения");
            }
            property.load(new FileInputStream("CreditService.properties"));

            this.CreditServiceDBHost = property.getProperty("creditService.db.host");
            this.CreditServiceDBLogin = property.getProperty("creditService.db.user");
            this.CreditServiceDBPass = property.getProperty("creditService.db.password");
            this.port = property.getProperty("creditService.port");
            this.securePhrase = property.getProperty("creditService.securePhrase");

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует! (Credits)");
        }
    }

    public static void createInstanceIfNotCreated() {
        if (instance == null) {
            instance = new Config();
        }
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

    public static String getPort() {
        createInstanceIfNotCreated();
        return instance.port;
    }

    public static String getSecurePhrase() {
        createInstanceIfNotCreated();
        return instance.securePhrase;
    }

}

