package database;

import com.github.youngteurus.servletdatabase.database.BasePostgresDataBase;
import config.Config;

public class CreditPostgresDataBase extends BasePostgresDataBase {
    private static CreditPostgresDataBase instance;


    private CreditPostgresDataBase(String dbURL, String user, String pass) {
        super(dbURL, user, pass);
    }

    public static CreditPostgresDataBase getInstance() {
        if (instance == null) {
            instance = new CreditPostgresDataBase(
                    Config.getCreditServiceDBHost(),
                    Config.getCreditServiceDBLogin(),
                    Config.getCreditServiceDBPass()
            );
        }
        return instance;
    }
}
