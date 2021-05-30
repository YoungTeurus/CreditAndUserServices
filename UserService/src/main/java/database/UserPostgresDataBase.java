package database;

import com.github.youngteurus.servletdatabase.database.BasePostgresDataBase;
import com.github.youngteurus.servletdatabase.database.DataBase;
import config.Config;

public class UserPostgresDataBase extends BasePostgresDataBase {
    private static DataBase instance;

    private UserPostgresDataBase(String dbURL, String user, String pass){
        super(dbURL, user, pass);
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new UserPostgresDataBase(
                    Config.getUserServiceDBHost(),
                    Config.getUserServiceDBLogin(),
                    Config.getUserServiceDBPass()
            );
        }
        return instance;
    }
}
