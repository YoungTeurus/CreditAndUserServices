package database;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserPostgresDataBaseTest {
    private static DataBase userPostgresDataBase = UserPostgresDataBase.getInstance();

    @Test
    void testInsertReturn() throws DataBaseConnectionException, SQLException {
        Connection connection = userPostgresDataBase.getConnection();

        String sql = "INSERT INTO parents (\"parentId\", \"childId\") VALUES (1, 3)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // ResultSet rs = preparedStatement.executeQuery();
        int result = preparedStatement.executeUpdate();

        // while (rs.next()){
        //     System.out.println(rs);
        // }

        System.out.println(result);
    }
}