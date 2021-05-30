package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config instance;

    private String CreditServiceDBHost;
    private String CreditServiceDBLogin;
    private String CreditServiceDBPass;

    private Config() {
        if (instance == null) {
            instance = this;
        }
        load();
    }

    private void load() {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("../CreditService/src/main/resources/CreditService.properties"));

            this.CreditServiceDBHost = property.getProperty("creditService.db.host");
            this.CreditServiceDBLogin = property.getProperty("creditService.db.user");
            this.CreditServiceDBPass = property.getProperty("creditService.db.password");

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

}

