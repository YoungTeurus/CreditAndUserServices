package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataBase {
    ResultSet executeStatement(PreparedStatement preparedStatement) throws SQLException;

    Connection getConnection() throws DataBaseConnectionException;
}
